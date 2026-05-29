package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fines")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Fine {

    public enum Status { UNPAID, PAID, WAIVED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "fine_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal fineAmount;

    @Column(name = "days_overdue", nullable = false)
    private Integer daysOverdue;

    @Column(name = "fine_per_day", precision = 10, scale = 2)
    private BigDecimal finePerDay = new BigDecimal("2000");

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Status status = Status.UNPAID;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private User paidBy;
}