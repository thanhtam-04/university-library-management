package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thuvien.entity.Fine;
import thuvien.entity.Fine.Status;
import thuvien.entity.Payment;
import thuvien.entity.Payment.PaymentMethod;
import thuvien.entity.Payment.PaymentType;
import thuvien.entity.Member;
import thuvien.entity.Loan; // Đã thêm
import thuvien.repository.FineRepository;
import thuvien.repository.PaymentRepository;
import thuvien.repository.MemberRepository;
import thuvien.repository.LoanRepository; // Đã thêm

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate; // Đã thêm
import java.time.temporal.ChronoUnit; // Đã thêm
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository; // Đã thêm

    public List<Fine> findAll() { return fineRepository.findAll(); }

    public Fine findById(Long id) {
        return fineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu phạt ID: " + id));
    }

    public List<Fine> findByMember(Long memberId) {
        return fineRepository.findByMemberId(memberId);
    }

    public List<Fine> findByStatus(Status status) {
        return fineRepository.findByStatus(status);
    }

    public void save(Fine fine) { fineRepository.save(fine); }

    /**
     * Tự động quét các phiếu mượn quá hạn và tạo phiếu phạt nếu chưa có
     */
    /**
     * Tự động quét các phiếu mượn quá hạn:
     * 1. Tạo phiếu phạt cho các phiếu chưa có phạt.
     * 2. Cập nhật số ngày trễ và số tiền cho các phiếu đang trong trạng thái UNPAID.
     */
    @Transactional
    public void syncOverdueLoans() {
        // 1. Quét các phiếu ACTIVE để kiểm tra quá hạn và tạo phiếu phạt mới
        List<Loan> activeLoans = loanRepository.findByStatus(Loan.Status.ACTIVE);
        
        for (Loan loan : activeLoans) {
            if (LocalDate.now().isAfter(loan.getDueDate())) {
                // Chuyển trạng thái sang OVERDUE
                loan.setStatus(Loan.Status.OVERDUE);
                loanRepository.save(loan);
                
                // Tạo mới nếu chưa có phiếu phạt
                if (!fineRepository.existsByLoanId(loan.getId())) {
                    Fine newFine = new Fine();
                    newFine.setLoan(loan);
                    newFine.setMember(loan.getMember());
                    
                    long days = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
                    newFine.setDaysOverdue((int) days);
                    
                    BigDecimal feePerDay = new BigDecimal("2000");
                    newFine.setFinePerDay(feePerDay);
                    newFine.setFineAmount(feePerDay.multiply(BigDecimal.valueOf(days)));
                    newFine.setStatus(Fine.Status.UNPAID);
                    newFine.setCreatedAt(LocalDateTime.now());
                    
                    fineRepository.save(newFine);
                }
            }
        }

        // 2. CẬP NHẬT LẠI CÁC PHIẾU ĐANG CHỜ (UNPAID) để số ngày trễ tăng theo thực tế
        List<Fine> unpaidFines = fineRepository.findByStatus(Fine.Status.UNPAID);
        for (Fine fine : unpaidFines) {
            if (fine.getLoan() != null) {
                long days = ChronoUnit.DAYS.between(fine.getLoan().getDueDate(), LocalDate.now());
                
                // Chỉ cập nhật nếu số ngày trễ thực tế khác với dữ liệu cũ trong DB
                if (days > 0 && days != fine.getDaysOverdue()) {
                    fine.setDaysOverdue((int) days);
                    // Cập nhật lại số tiền phạt mới
                    fine.setFineAmount(fine.getFinePerDay().multiply(BigDecimal.valueOf(days)));
                    fineRepository.save(fine);
                }
            }
        }
    
}
    @Transactional
    public void processOverdueLocks() {
        List<Fine> unpaidFines = fineRepository.findAll();
        
        for (Fine fine : unpaidFines) {
            // Log ra để kiểm tra xem nó có quét thấy dữ liệu không
            System.out.println("DEBUG: Đang kiểm tra fine ID: " + fine.getId() + " - Ngày trễ: " + fine.getDaysOverdue());
            
            // Kiểm tra điều kiện: Phải là UNPAID và trễ > 10 ngày
            if (fine.getStatus() == Fine.Status.UNPAID && fine.getDaysOverdue() > 10) {
                Member member = fine.getMember();
                if (member != null) {
                    System.out.println("DEBUG: Khóa thành viên ID: " + member.getId());
                    member.setAccountLocked(true); 
                    member.setStatus(Member.Status.LOCKED);
                    memberRepository.save(member);
                }
            }
        }
    }
    /**
     * Xử lý thanh toán phí phạt:
     * 1. Cập nhật trạng thái phiếu phạt thành PAID
     * 2. Tạo lịch sử giao dịch hóa đơn bên bảng payments
     * 3. Trừ nợ (currentDebt) của độc giả xuống
     */
    @Transactional
    public void markAsPaid(Long fineId, thuvien.entity.User paidByUser, String methodStr) {
        Fine fine = findById(fineId);
        if (fine.getStatus() != Status.UNPAID) {
            throw new RuntimeException("Phiếu phạt này đã được xử lý từ trước!");
        }

        // 1. Cập nhật thông tin phiếu phạt
        fine.setStatus(Status.PAID);
        fine.setPaidAt(LocalDateTime.now());
        fine.setPaidBy(paidByUser);
        fineRepository.save(fine);

        // 2. Tự động sinh một bản ghi thanh toán sang bảng payments
        Payment payment = new Payment();
        payment.setMember(fine.getMember());
        payment.setFine(fine);
        payment.setAmount(fine.getFineAmount());
        payment.setPaymentType(PaymentType.FINE);
        
        PaymentMethod method = PaymentMethod.CASH;
        if (methodStr != null && methodStr.equalsIgnoreCase("TRANSFER")) {
            method = PaymentMethod.TRANSFER;
        }
        payment.setPaymentMethod(method);
        payment.setNote("Thu phí phạt vi phạm phiếu mượn mã: " + (fine.getLoan() != null ? fine.getLoan().getLoanCode() : "#" + fine.getId()));
        payment.setProcessedBy(paidByUser);
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // 3. Khấu trừ giảm nợ trực tiếp trên tài khoản Member
        Member member = fine.getMember();
        if (member != null) {
            BigDecimal currentDebt = member.getCurrentDebt() != null ? member.getCurrentDebt() : BigDecimal.ZERO;
            BigDecimal newDebt = currentDebt.subtract(fine.getFineAmount());
            member.setCurrentDebt(newDebt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newDebt);
            memberRepository.save(member);
        }
    }

    @Transactional
    public void waive(Long fineId) {
        Fine fine = findById(fineId);
        if (fine.getStatus() != Status.UNPAID) {
            throw new RuntimeException("Phiếu phạt này đã được xử lý từ trước!");
        }
        fine.setStatus(Status.WAIVED);
        fineRepository.save(fine);
    }
}