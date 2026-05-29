package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thuvien.entity.BookCopy;
import thuvien.entity.BookCopy.Status;
import thuvien.repository.BookCopyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;

    /* ── READ ── */

    public List<BookCopy> findAll() {
        return bookCopyRepository.findAll();
    }

    public BookCopy findById(Long id) {
        return bookCopyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản sao id=" + id));
    }

    public BookCopy findByBarcode(String barcode) {
        return bookCopyRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy barcode: " + barcode));
    }

    /** Tất cả bản sao của một đầu sách */
    public List<BookCopy> findByBookId(Long bookId) {
        return bookCopyRepository.findByBookId(bookId);
    }

    /** Chỉ lấy bản sao đang sẵn sàng cho mượn */
    public List<BookCopy> findAvailableByBookId(Long bookId) {
        return bookCopyRepository.findByBookIdAndStatus(bookId, Status.AVAILABLE);
    }

    /** Tất cả bản sao đang sẵn sàng */
    public List<BookCopy> findAllAvailable() {
        return bookCopyRepository.findByStatus(Status.AVAILABLE);
    }

    public long countByStatus(Status status) {
        return bookCopyRepository.countByStatus(status);
    }

    /* ── WRITE ── */

    @Transactional
    public BookCopy save(BookCopy copy) {
        return bookCopyRepository.save(copy);
    }

    @Transactional
    public BookCopy update(BookCopy copy) {
        BookCopy existing = findById(copy.getId());
        existing.setBarcode(copy.getBarcode());
        existing.setStatus(copy.getStatus());
        existing.setConditionNote(copy.getConditionNote());
        existing.setAcquiredDate(copy.getAcquiredDate());
        return bookCopyRepository.save(existing);
    }

    /**
     * Đổi trạng thái bản sao — dùng khi cho mượn / trả sách.
     */
    @Transactional
    public void changeStatus(Long copyId, Status newStatus) {
        BookCopy copy = findById(copyId);
        copy.setStatus(newStatus);
        bookCopyRepository.save(copy);
    }

    @Transactional
    public void deleteById(Long id) {
        bookCopyRepository.deleteById(id);
    }

    /* ── UTIL ── */

    public boolean existsByBarcode(String barcode) {
        return bookCopyRepository.findByBarcode(barcode).isPresent();
    }
}