package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thuvien.entity.Fine;
import thuvien.entity.Fine.Status;

import java.math.BigDecimal;
import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByMemberId(Long memberId);
    List<Fine> findByStatus(Status status);
    List<Fine> findByLoanId(Long loanId);
    long countByStatus(Status status);

    // Sử dụng JPQL để tính tổng số tiền phạt
    // Thêm COALESCE để tránh lỗi trả về null khi không có dữ liệu
    @Query("SELECT COALESCE(SUM(f.fineAmount), 0) FROM Fine f")
    BigDecimal sumFineAmount();
 // Trong file FineRepository.java
    boolean existsByLoanId(Long loanId);
 // Thêm vào FineRepository.java
    @Query("SELECT COALESCE(SUM(f.fineAmount), 0) FROM Fine f WHERE f.loan.id = :loanId")
    BigDecimal sumFineAmountByLoanId(@org.springframework.data.repository.query.Param("loanId") Long loanId);
 // Trong FineRepository.java
    @Query("SELECT COALESCE(SUM(f.fineAmount), 0) FROM Fine f WHERE f.status = 'UNPAID'")
    BigDecimal sumUnpaidFineAmount();
    
}