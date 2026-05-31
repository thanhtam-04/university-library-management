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
  @Transactional
public void syncOverdueLoans() {
    // 1. Lấy tất cả các phiếu ĐANG MƯỢN (ACTIVE)
    List<Loan> activeLoans = loanRepository.findByStatus(Loan.Status.ACTIVE);
    
    for (Loan loan : activeLoans) {
        // 2. Tự kiểm tra: Nếu ngày quá hạn đã qua (today > due_date)
        if (LocalDate.now().isAfter(loan.getDueDate())) {
            
            // Chuyển trạng thái phiếu sang OVERDUE trong DB
            loan.setStatus(Loan.Status.OVERDUE);
            loanRepository.save(loan);
            
            // 3. Tạo phiếu phạt nếu chưa tồn tại
            if (!fineRepository.existsByLoanId(loan.getId())) {
                Fine newFine = new Fine();
                newFine.setLoan(loan);
                newFine.setMember(loan.getMember());
                
                long days = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
                newFine.setDaysOverdue((int) days);
                
                BigDecimal feePerDay = new BigDecimal("2000");
                newFine.setFineAmount(feePerDay.multiply(BigDecimal.valueOf(days)));
                newFine.setFinePerDay(feePerDay);
                newFine.setStatus(Fine.Status.UNPAID);
                newFine.setCreatedAt(LocalDateTime.now());
                
                fineRepository.save(newFine);
                System.out.println("--- ĐÃ TỰ ĐỘNG CẬP NHẬT PHIẾU QUÁ HẠN: " + loan.getLoanCode() + " ---");
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