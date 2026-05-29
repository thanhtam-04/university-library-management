package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import thuvien.dto.response.StatisticsResponse;
import thuvien.entity.Fine;
import thuvien.entity.Loan;
import thuvien.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final BookRepository     bookRepository;
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

    /**
     * Trả về danh sách [tên sách, số lần mượn] giảm dần, tối đa `limit` cuốn.
     */
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

    /**
     * Top thành viên nợ nhiều nhất.
     */
    public List<Map<String, Object>> topDebtMembers(int limit) {
        return memberRepository.findAll().stream()
                .filter(m -> m.getCurrentDebt() != null
                        && m.getCurrentDebt().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(
                        thuvien.entity.Member::getCurrentDebt).reversed())
                .limit(limit)
                .map(m -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("name",    m.getUser() != null ? m.getUser().getFullName() : "—");
                    map.put("card",    m.getCardNumber());
                    map.put("debt",    m.getCurrentDebt());
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * Phân bố phiếu phạt theo trạng thái cho biểu đồ Doughnut.
     * Trả về map: "UNPAID" → count, "PAID" → count, "WAIVED" → count
     */
    public Map<String, Long> fineStatusDistribution() {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("Chưa thanh toán", fineRepository.countByStatus(Fine.Status.UNPAID));
        result.put("Đã thanh toán",   fineRepository.countByStatus(Fine.Status.PAID));
        result.put("Miễn giảm",       fineRepository.countByStatus(Fine.Status.WAIVED));
        return result;
    }
    
	public StatisticsResponse getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}
}