package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Loan {

    // Thêm PENDING vào enum để Controller nhận diện được trạng thái chờ duyệt online
    public enum Status { PENDING, ACTIVE, RETURNED, OVERDUE, LOST }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_code", unique = true, nullable = false, length = 30)
    private String loanCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    // ─── FIX LỖI TẠI ĐÂY: Đổi optional từ false thành true để cho phép trống thủ thư khi đăng ký online ───
    @ManyToOne(optional = true)
    @JoinColumn(name = "librarian_id", nullable = true)
    private User librarian;

    @Column(name = "loan_date")
    private LocalDateTime loanDate = LocalDateTime.now();

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 15) // Tăng độ dài lên 15 để chứa vừa chữ PENDING
    private Status status = Status.PENDING; // Mặc định khi mượn online sẽ là PENDING (Chờ duyệt)

    @Column(name = "deposit_paid", precision = 12, scale = 2)
    private BigDecimal depositPaid = BigDecimal.ZERO;

    @Column(name = "deposit_refunded", precision = 12, scale = 2)
    private BigDecimal depositRefunded = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<LoanItem> items = new ArrayList<>();
    public enum DepositStatus { NONE, UNPAID, PAID, REFUNDED }

    @Enumerated(EnumType.STRING)
    @Column(name = "deposit_status", length = 20)
    private DepositStatus depositStatus = DepositStatus.NONE;
}