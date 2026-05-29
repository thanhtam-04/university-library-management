package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thuvien.entity.Member;
import thuvien.entity.Payment;
import thuvien.entity.Payment.PaymentType;
import thuvien.repository.MemberRepository;
import thuvien.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository  memberRepository;

    /* ── READ ── */

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy payment id=" + id));
    }

    /** Lịch sử thanh toán của một thành viên */
    public List<Payment> findByMemberId(Long memberId) {
        return paymentRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
    }

    /** Thanh toán theo loại */
    public List<Payment> findByType(PaymentType type) {
        return paymentRepository.findByPaymentType(type);
    }

    /** Tổng thu theo loại */
    public BigDecimal sumByType(PaymentType type) {
        BigDecimal total = paymentRepository.sumAmountByPaymentType(type);
        return total != null ? total : BigDecimal.ZERO;
    }

    /** Tổng thu trong ngày hôm nay */
    public BigDecimal sumToday() {
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end   = start.plusDays(1);
        BigDecimal total = paymentRepository.sumAmountBetween(start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    /* ── WRITE ── */

    /**
     * Lưu một giao dịch thanh toán và cập nhật currentDebt của Member.
     * - FINE        → tăng debt
     * - DEPOSIT     → tăng debt (cọc)
     * - DEPOSIT_REFUND → giảm debt (hoàn cọc)
     */
    @Transactional
    public Payment save(Payment payment) {
        payment.setCreatedAt(LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);
        updateMemberDebt(payment.getMember(), payment.getPaymentType(), payment.getAmount());
        return saved;
    }

    @Transactional
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    /* ── PRIVATE HELPERS ── */

    private void updateMemberDebt(Member member, PaymentType type, BigDecimal amount) {
        Member m = memberRepository.findById(member.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy member"));

        BigDecimal current = m.getCurrentDebt() != null ? m.getCurrentDebt() : BigDecimal.ZERO;

        switch (type) {
            case FINE, DEPOSIT -> m.setCurrentDebt(current.add(amount));
            case DEPOSIT_REFUND -> {
                BigDecimal newDebt = current.subtract(amount);
                m.setCurrentDebt(newDebt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newDebt);
            }
        }
        memberRepository.save(m);
    }
}