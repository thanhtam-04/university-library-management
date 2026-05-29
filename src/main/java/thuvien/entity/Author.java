package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(length = 100)
    private String pseudonym;
    @Column(name = "image_url", length = 255)
    private String imageUrl;
    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 100)
    private String nationality;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Bổ sung quan hệ Many-to-Many với Book.
     * mappedBy = "authors" trỏ tới biến Set<Author> authors trong file Book.java
     */
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();

	public Object getAvatar() {
		// TODO Auto-generated method stub
		return null;
	}

}