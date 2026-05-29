package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String isbn;

    @Column(nullable = false, length = 500)
    private String title;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(length = 50)
    private String edition;

    @Column(length = 50)
    private String language = "Tiếng Việt";

    @Column(name = "total_copies")
    private Integer totalCopies = 0;

    @Column(name = "available_copies")
    private Integer availableCopies = 0;
    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "shelf_location", length = 50)
    private String shelfLocation;

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;
  
    @Column(name = "deposit_amount")
    private BigDecimal depositAmount; // Đảm bảo tên biến là depositAmount
    @Column(name = "deposit_fee", precision = 12, scale = 2)
    private BigDecimal depositFee = BigDecimal.ZERO;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
 // =========================================================================
    // HÀM BỔ SUNG: Chuyển đổi danh sách tác giả (Set<Author>) thành chuỗi String
    // để giao diện Thymeleaf gọi an toàn khi mượn sách online (chưa có BookCopy)
    // =========================================================================
    public String getAuthorString() {
        if (this.authors == null || this.authors.isEmpty()) {
            return "N/A";
        }
        return this.authors.stream()
                .map(author -> {
                    // Nếu thực thể Author dùng 'fullName' thì để '.getFullName()', nếu dùng 'name' thì sửa lại cho đúng
                    return author.getFullName() != null ? author.getFullName() : "Tác giả ẩn danh";
                })
                .collect(java.util.stream.Collectors.joining(", "));
    }

 // =========================================================================
    // RÀNG BUỘC KHO: Số bản có sẵn không được lớn hơn tổng số bản sao hiện có
    // =========================================================================
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        validateCopies();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        validateCopies();
    }

    private void validateCopies() {
        if (this.totalCopies == null) this.totalCopies = 0;
        if (this.availableCopies == null) this.availableCopies = 0;
        
        if (this.availableCopies > this.totalCopies) {
            throw new IllegalArgumentException("Lỗi hệ thống: Số bản có sẵn (" + this.availableCopies + ") không thể vượt quá tổng số bản sao (" + this.totalCopies + ")!");
        }
        if (this.availableCopies < 0) {
            throw new IllegalArgumentException("Lỗi hệ thống: Số bản có sẵn không thể nhỏ hơn 0!");
        }
    }
    
}