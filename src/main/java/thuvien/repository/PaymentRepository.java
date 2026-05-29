package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import thuvien.entity.Payment;
import thuvien.entity.Payment.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByMemberId(Long memberId);
    List<Payment> findByFineId(Long fineId);
    List<Payment> findByMemberIdOrderByCreatedAtDesc(Long memberId);
    List<Payment> findByPaymentType(PaymentType type);

    // 1. Sửa lỗi tính tổng theo loại thanh toán
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentType = :type")
    BigDecimal sumAmountByPaymentType(@Param("type") PaymentType type);

    // 2. Sửa lỗi tính tổng trong khoảng thời gian
    // Giả sử trong Entity Payment Nhi đặt tên trường thời gian là 'createdAt'
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.createdAt BETWEEN :start AND :end")
    BigDecimal sumAmountBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}