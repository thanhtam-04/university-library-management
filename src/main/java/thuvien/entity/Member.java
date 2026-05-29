package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "members")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Member {

    public enum Status { ACTIVE, SUSPENDED, EXPIRED, LOCKED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "card_number", unique = true, nullable = false, length = 20)
    private String cardNumber;

    @Column(name = "student_code", unique = true, length = 20)
    private String studentCode;

    @Column(length = 200)
    private String department;

    @Column(length = 50)
    private String course;

    @Column(name = "card_issued_date", nullable = false)
    private LocalDate cardIssuedDate;

    @Column(name = "card_expiry_date", nullable = false)
    private LocalDate cardExpiryDate;

    @Column(name = "max_borrow_limit")
    private Integer maxBorrowLimit = 5;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Status status = Status.ACTIVE;

    @Column(name = "total_borrowed")
    private Integer totalBorrowed = 0;

    @Column(name = "current_debt", precision = 12, scale = 2)
    private BigDecimal currentDebt = BigDecimal.ZERO;

	public void setFullName(String fullName) {
		// TODO Auto-generated method stub
		
	}

	public void setEmail(String email) {
		// TODO Auto-generated method stub
		
	}

	// Xóa các hàm TODO rỗng cũ và thay bằng đoạn này:

	public void setExpiryDate(LocalDate expiryDate) {
	    this.cardExpiryDate = expiryDate;
	}

	public void setIsActive(boolean active) {
	    this.status = active ? Status.ACTIVE : Status.LOCKED;
	}
}