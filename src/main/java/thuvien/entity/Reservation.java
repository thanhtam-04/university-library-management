package thuvien.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(nullable = false)
    private LocalDateTime reservationDate = LocalDateTime.now();
    
    @Column(length = 20)
    private String status = "PENDING"; 

    @Column(columnDefinition = "TEXT")
    private String note;

    private LocalDateTime notifiedDate;
}