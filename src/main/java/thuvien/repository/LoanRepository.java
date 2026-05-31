package thuvien.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thuvien.entity.Loan;
import thuvien.entity.Loan.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    // CẮT BỎ "LEFT JOIN FETCH l.items" để dứt điểm lỗi vòng lặp vô hạn
    @Query("SELECT DISTINCT l FROM Loan l " +
           "LEFT JOIN FETCH l.member m " +
           "LEFT JOIN FETCH m.user u " +
           "ORDER BY l.loanDate DESC")
    List<Loan> findAllWithDetails();

    Optional<Loan> findByLoanCode(String loanCode);
    
    List<Loan> findByMemberId(Long memberId);
    
    List<Loan> findByStatus(Status status);
    
    List<Loan> findByMemberIdAndStatus(Long memberId, Status status);
    
    long countByStatus(Status status);

    @Query("SELECT l FROM Loan l ORDER BY l.loanDate DESC")
    List<Loan> findRecentLoans(Pageable pageable);
    List<Loan> findByStatus(String status);
}