package thuvien.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thuvien.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);
    
    @EntityGraph(attributePaths = {"authors", "category", "publisher"})
    List<Book> findByCategoryId(Long categoryId);

    List<Book> findByPublisherId(Long publisherId);

    // Dùng cho trang chủ
    @EntityGraph(attributePaths = {"authors", "category", "publisher"})
    List<Book> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "category", "publisher"})
    List<Book> findAllByOrderByIdDesc(Pageable pageable);

    // Phân trang + eager load
    @EntityGraph(attributePaths = {"authors", "category", "publisher"})
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "category", "publisher"})
    Page<Book> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT b FROM Book b ORDER BY b.createdAt DESC")
    List<Book> findTopNewBooks(Pageable pageable);
    @EntityGraph(attributePaths = {"authors", "category", "publisher"})
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) OR b.isbn LIKE CONCAT('%', :title, '%')) AND " +
           "(:author IS NULL OR EXISTS (SELECT a FROM b.authors a WHERE LOWER(a.fullName) LIKE LOWER(CONCAT('%', :author, '%')))) AND " +
           "(:categoryId IS NULL OR b.category.id = :categoryId) AND " +
           "(:status IS NULL OR " +
           "  (:status = 'available' AND b.availableCopies > 0) OR " +
           "  (:status = 'unavailable' AND b.availableCopies = 0)" +
           ")")
    List<Book> filterBooksAdvanced(@Param("title") String title,
                                   @Param("author") String author,
                                   @Param("categoryId") Long categoryId,
                                   @Param("status") String status);
}
    
