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

    public enum Status { PENDING, ACTIVE, RETURNED, OVERDUE, LOST }
    public enum DepositStatus { NONE, UNPAID, PAID, REFUNDED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_code", unique = true, nullable = false, length = 30)
    private String loanCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    // Cho phép trống thủ thư khi đăng ký online
    @ManyToOne(optional = true)
    @JoinColumn(name = "librarian_id", nullable = true)
    private User librarian;

    // ĐÃ SỬA: Bỏ = LocalDateTime.now() để tránh ghi đè ngày tạo phiếu
    @Column(name = "loan_date")
    private LocalDateTime loanDate;

    @Column(name = "due_date", nullable = true)
    private LocalDate dueDate;

    @Column(name = "rental_fee", precision = 12, scale = 2)
    private BigDecimal rentalFee = BigDecimal.ZERO;

    @Column(name = "deposit_amount", precision = 12, scale = 2)
    private BigDecimal depositAmount = BigDecimal.ZERO;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 15) 
    private Status status = Status.PENDING; 

    @Column(name = "deposit_paid", precision = 12, scale = 2)
    private BigDecimal depositPaid = BigDecimal.ZERO;

    @Column(name = "deposit_refunded", precision = 12, scale = 2)
    private BigDecimal depositRefunded = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<LoanItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "deposit_status", length = 20)
    private DepositStatus depositStatus = DepositStatus.NONE;

    public BigDecimal getTotalAmount() {
        BigDecimal rental = (rentalFee != null) ? rentalFee : BigDecimal.ZERO;
        BigDecimal deposit = (depositPaid != null) ? depositPaid : BigDecimal.ZERO;
        
        // Cộng thêm 50,000 VNĐ phí mượn mặc định
        BigDecimal defaultRentalFee = new BigDecimal("50000");
        
        return rental.add(deposit).add(defaultRentalFee);
    }
}