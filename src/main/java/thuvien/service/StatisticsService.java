package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import thuvien.dto.response.StatisticsResponse;
import thuvien.entity.Fine;
import thuvien.entity.Loan;
import thuvien.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final BookRepository       bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final MemberRepository   memberRepository;
    private final LoanRepository     loanRepository;
    private final FineRepository     fineRepository;
    private final PaymentRepository  paymentRepository;
    private final LoanItemRepository loanItemRepository;

    /* ══════════════════════════════════════════
       TỔNG QUAN
    ══════════════════════════════════════════ */

    public long totalBooks()       { return bookRepository.count(); }
    public long totalCopies()      { return bookCopyRepository.count(); }
    public long totalMembers()     { return memberRepository.count(); }
    public long totalLoans()       { return loanRepository.count(); }

    public long activeLoans() {
        return loanRepository.countByStatus(Loan.Status.ACTIVE);
    }

    public long overdueLoans() {
        return loanRepository.countByStatus(Loan.Status.OVERDUE);
    }

    public long unpaidFines() {
        return fineRepository.countByStatus(Fine.Status.UNPAID);
    }

    public BigDecimal totalDebt() {
        return memberRepository.findAll().stream()
                .map(m -> m.getCurrentDebt() != null ? m.getCurrentDebt() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalFineAmount() {
        BigDecimal total = fineRepository.sumFineAmount();
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal totalPaidAmount() {
        BigDecimal total = paymentRepository.sumAmountByPaymentType(
                thuvien.entity.Payment.PaymentType.FINE);
        return total != null ? total : BigDecimal.ZERO;
    }
    public BigDecimal calculateActualTotalDebt() {
        return fineRepository.sumUnpaidFineAmount();
    }

    /* ══════════════════════════════════════════
       BIỂU ĐỒ MƯỢN/TRẢ THEO THÁNG (năm hiện tại)
    ══════════════════════════════════════════ */

    /**
     * Trả về map tháng → số phiếu mượn tạo trong tháng đó.
     * Key: "T1" .. "T12", Value: số phiếu
     */
    public Map<String, Long> loansByMonth() {
        int year = LocalDate.now().getYear();
        List<Loan> allLoans = loanRepository.findAll();

        Map<String, Long> result = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) {
            result.put("T" + m, 0L);
        }

        allLoans.stream()
                .filter(l -> l.getLoanDate() != null
                        && l.getLoanDate().getYear() == year)
                .forEach(l -> {
                    String key = "T" + l.getLoanDate().getMonthValue();
                    result.merge(key, 1L, Long::sum);
                });

        return result;
    }

    /**
     * Trả về map tháng → số phiếu đã trả trong tháng đó.
     */
    public Map<String, Long> returnsByMonth() {
        int year = LocalDate.now().getYear();
        List<Loan> allLoans = loanRepository.findAll();

        Map<String, Long> result = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) {
            result.put("T" + m, 0L);
        }

        allLoans.stream()
                .filter(l -> l.getReturnDate() != null
                        && l.getReturnDate().getYear() == year)
                .forEach(l -> {
                    String key = "T" + l.getReturnDate().getMonthValue();
                    result.merge(key, 1L, Long::sum);
                });

        return result;
    }

    /* ══════════════════════════════════════════
       TOP SÁCH MƯỢN NHIỀU NHẤT
    ══════════════════════════════════════════ */

    public List<Map<String, Object>> topBorrowedBooks(int limit) {
        return loanItemRepository.findAll().stream()
                .filter(item -> item.getBookCopy() != null
                        && item.getBookCopy().getBook() != null)
                .collect(Collectors.groupingBy(
                        item -> item.getBookCopy().getBook().getTitle(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(e -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("title", e.getKey());
                    m.put("count", e.getValue());
                    return m;
                })
                .collect(Collectors.toList());
    }

    /* ══════════════════════════════════════════
       THỐNG KÊ PHIẾU PHẠT
    ══════════════════════════════════════════ */

    public long fineCountByStatus(Fine.Status status) {
        return fineRepository.countByStatus(status);
    }

    public List<Map<String, Object>> topDebtMembers(int limit) {
        // Lấy tất cả các phiếu phạt chưa thanh toán
        List<Fine> unpaidFines = fineRepository.findByStatus(Fine.Status.UNPAID);
        
        // Nhóm theo Member và tính tổng tiền nợ
        return unpaidFines.stream()
                .filter(f -> f.getMember() != null)
                .collect(Collectors.groupingBy(Fine::getMember, 
                         Collectors.reducing(BigDecimal.ZERO, Fine::getFineAmount, BigDecimal::add)))
                .entrySet().stream()
                .sorted(Map.Entry.<thuvien.entity.Member, BigDecimal>comparingByValue().reversed())
                .limit(limit)
                .map(e -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("name", e.getKey().getUser() != null ? e.getKey().getUser().getFullName() : "—");
                    map.put("card", e.getKey().getCardNumber());
                    map.put("debt", e.getValue()); // Nợ thực tế từ bảng Fine
                    return map;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Long> fineStatusDistribution() {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("Chưa thanh toán", fineRepository.countByStatus(Fine.Status.UNPAID));
        result.put("Đã thanh toán",   fineRepository.countByStatus(Fine.Status.PAID));
        result.put("Miễn giảm",       fineRepository.countByStatus(Fine.Status.WAIVED));
        return result;
    }

    public StatisticsResponse getStatistics() {
        return StatisticsResponse.builder()
                .totalBooks(totalBooks())
                .availableCopies(bookCopyRepository.count())
                .activeMembers(totalMembers())
                .overdueLoans(overdueLoans())
                .build();
    }

    /* ══════════════════════════════════════════
       BỔ SUNG: THỐNG KÊ DOANH THU THEO THÁNG
    ══════════════════════════════════════════ */
    public Map<String, Long> getMonthlyRevenue() {
        int currentYear = LocalDate.now().getYear();
        List<Loan> allLoans = loanRepository.findAll();

        Map<String, Long> result = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) {
            result.put("T" + m, 0L);
        }

        allLoans.stream()
                // Lọc theo trạng thái RETURNED (đảm bảo đúng Enum trong Loan.java)
                .filter(l -> l.getStatus() == Loan.Status.RETURNED && l.getReturnDate() != null 
                        && l.getReturnDate().getYear() == currentYear)
                .forEach(l -> {
                    String key = "T" + l.getReturnDate().getMonthValue();
                    
                    // Gọi Repository lấy tổng tiền phạt cho phiếu mượn này
                    BigDecimal fineSum = fineRepository.sumFineAmountByLoanId(l.getId());
                    long fine = (fineSum != null) ? fineSum.longValue() : 0L;
                    
                    long revenue = 50000L + fine; // Phí mượn 50k + tiền phạt
                    result.merge(key, revenue, Long::sum);
                });

        return result;
    }
    public BigDecimal getTotalRevenue() {
        // Lấy tất cả các phiếu đã trả
        List<Loan> returnedLoans = loanRepository.findAll().stream()
            .filter(l -> "RETURNED".equals(l.getStatus().toString()))
            .collect(Collectors.toList());

        BigDecimal totalRentalFee = new BigDecimal("50000").multiply(new BigDecimal(returnedLoans.size()));
        
        // Chỉ cộng tiền phạt đã thu (status = PAID)
        BigDecimal totalCollectedFine = fineRepository.findAll().stream()
            .filter(f -> f.getStatus() == Fine.Status.PAID)
            .map(Fine::getFineAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalRentalFee.add(totalCollectedFine);
    }
    /**
     * Tính tổng nợ = Tổng tiền các phiếu phạt có trạng thái UNPAID
     */
 // Cập nhật lại hàm này trong StatisticsService.java
    
}