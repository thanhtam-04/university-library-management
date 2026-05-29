package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thuvien.entity.BookCopy;
import thuvien.entity.LoanItem;
import thuvien.repository.LoanItemRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanItemService {

    private final LoanItemRepository loanItemRepository;
    private final BookCopyService    bookCopyService;

    /* ── READ ── */

    public List<LoanItem> findAll() {
        return loanItemRepository.findAll();
    }

    public LoanItem findById(Long id) {
        return loanItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loan item id=" + id));
    }

    /** Tất cả item của một phiếu mượn */
    public List<LoanItem> findByLoanId(Long loanId) {
        return loanItemRepository.findByLoanId(loanId);
    }

    /** Các item chưa trả của một phiếu mượn */
    public List<LoanItem> findUnreturnedByLoanId(Long loanId) {
        return loanItemRepository.findByLoanIdAndReturned(loanId, false);
    }

    /** Tất cả item chưa trả (toàn hệ thống) */
    public List<LoanItem> findAllUnreturned() {
        return loanItemRepository.findByReturned(false);
    }

    /** Kiểm tra bản sao có đang được mượn không */
    public boolean isCopyCurrentlyBorrowed(Long bookCopyId) {
        return loanItemRepository.existsByBookCopyIdAndReturned(bookCopyId, false);
    }

    /* ── WRITE ── */

    @Transactional
    public LoanItem save(LoanItem item) {
        return loanItemRepository.save(item);
    }

    /**
     * Trả một bản sao cụ thể:
     * 1. Đánh dấu item là đã trả + ghi ngày trả
     * 2. Đổi trạng thái BookCopy → AVAILABLE
     */
    @Transactional
    public LoanItem returnItem(Long itemId) {
        LoanItem item = findById(itemId);
        if (Boolean.TRUE.equals(item.getReturned())) {
            throw new RuntimeException("Bản sao này đã được trả trước đó.");
        }
        item.setReturned(true);
        item.setReturnDate(LocalDateTime.now());
        bookCopyService.changeStatus(item.getBookCopy().getId(), BookCopy.Status.AVAILABLE);
        return loanItemRepository.save(item);
    }

    /**
     * Đánh dấu bản sao bị mất:
     * 1. Đánh dấu item returned = true
     * 2. Đổi trạng thái BookCopy → LOST
     */
    @Transactional
    public LoanItem markAsLost(Long itemId) {
        LoanItem item = findById(itemId);
        item.setReturned(true);
        item.setReturnDate(LocalDateTime.now());
        bookCopyService.changeStatus(item.getBookCopy().getId(), BookCopy.Status.LOST);
        return loanItemRepository.save(item);
    }

    @Transactional
    public void deleteById(Long id) {
        loanItemRepository.deleteById(id);
    }
}