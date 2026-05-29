package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Payment {

    public enum PaymentType  { FINE, DEPOSIT, DEPOSIT_REFUND }
    public enum PaymentMethod { CASH, TRANSFER }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "fine_id")
    private Fine fine;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 20)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 10)
    private PaymentMethod paymentMethod = PaymentMethod.CASH;

    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne(optional = false)
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}