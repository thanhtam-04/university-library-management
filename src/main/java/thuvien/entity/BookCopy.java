package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "book_copies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BookCopy {

    public enum Status { AVAILABLE, BORROWED, LOST, DAMAGED, MAINTENANCE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(unique = true, nullable = false, length = 50)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status = Status.AVAILABLE;

    @Column(name = "condition_note", columnDefinition = "TEXT")
    private String conditionNote;

    @Column(name = "acquired_date")
    private LocalDate acquiredDate;
}