package thuvien.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import thuvien.entity.BookCopy;
import thuvien.entity.BookCopy.Status;
import java.util.List;
import java.util.Optional;
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    Optional<BookCopy> findByBarcode(String barcode);
    List<BookCopy> findByBookIdAndStatus(Long bookId, Status status);
    List<BookCopy> findByBookId(Long bookId);
	long countByStatus(Status status);
	List<BookCopy> findByStatus(Status available);
}