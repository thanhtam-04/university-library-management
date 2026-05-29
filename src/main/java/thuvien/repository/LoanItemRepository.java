package thuvien.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import thuvien.entity.LoanItem;
import java.util.List;
public interface LoanItemRepository extends JpaRepository<LoanItem, Long> {
    List<LoanItem> findByLoanId(Long loanId);
    List<LoanItem> findByBookCopyId(Long bookCopyId);
	List<LoanItem> findByLoanIdAndReturned(Long loanId, boolean b);
	List<LoanItem> findByReturned(boolean b);
	boolean existsByBookCopyIdAndReturned(Long bookCopyId, boolean b);
}