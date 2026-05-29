package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "digital_documents")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DigitalDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    
    // Loại: giaotrinh, luanvan, nckh, dethi...
    private String type; 
    
    private String category;
    private Integer year;
    private String fileUrl; // Đường dẫn đến file PDF hoặc Drive
    
    private Integer views = 0;
    private Integer downloads = 0;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}