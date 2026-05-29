package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_copy_id")
    private BookCopy bookCopy;

    @Column
    private Boolean returned = false;

    @Column(name = "return_date")
    private LocalDateTime returnDate;
}