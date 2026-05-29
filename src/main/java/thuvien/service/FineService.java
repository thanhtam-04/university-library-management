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
import thuvien.repository.FineRepository;
import thuvien.repository.PaymentRepository;
import thuvien.repository.MemberRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;

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
        payment.setPaymentType(PaymentType.FINE); // Loại thanh toán phạt tiền
        
        // Chuẩn hóa phương thức thanh toán truyền lên từ giao diện (CASH hoặc TRANSFER)
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
            // Đảm bảo số nợ không bị âm dưới 0đ
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