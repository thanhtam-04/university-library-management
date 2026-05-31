package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import thuvien.entity.LoanItem;
import java.util.List;

public interface LoanItemRepository extends JpaRepository<LoanItem, Long> {

    // Các hàm cũ của bạn
    List<LoanItem> findByLoanId(Long loanId);
    List<LoanItem> findByBookCopyId(Long bookCopyId);
    List<LoanItem> findByLoanIdAndReturned(Long loanId, boolean b);
    List<LoanItem> findByReturned(boolean b);
    boolean existsByBookCopyIdAndReturned(Long bookCopyId, boolean b);

    // HÀM MỚI ĐỂ LẤY TOP SÁCH MƯỢN NHIỀU NHẤT
    // Dùng @Query để tối ưu: đếm số lần xuất hiện của sách trong bảng loan_items
    @Query("SELECT bc.book.title, COUNT(li.id) as count " +
           "FROM LoanItem li JOIN li.bookCopy bc " +
           "GROUP BY bc.book.title " +
           "ORDER BY count DESC")
    List<Object[]> findTopBorrowedBooks(Pageable pageable);
}