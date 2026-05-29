package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thuvien.dto.response.LoanResponse;
import thuvien.entity.*;
import thuvien.entity.Loan.Status;
import thuvien.entity.Loan.DepositStatus; // Đảm bảo đã import Enum DepositStatus vừa thêm ở Entity
import thuvien.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository       loanRepository;
    private final LoanItemRepository   loanItemRepository;
    private final BookCopyRepository   bookCopyRepository;
    private final MemberRepository     memberRepository;
    private final FineRepository       fineRepository;
    private final BookRepository       bookRepository; 

    @Transactional(readOnly = true)
    public List<Loan> findAll() { 
        return loanRepository.findAll(); 
    }

    @Transactional(readOnly = true)
    public Loan findById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Loan> findByMember(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<Loan> findByStatus(Status status) {
        return loanRepository.findByStatus(status);
    }

    @Transactional
    public void save(Loan loan) { 
        loanRepository.save(loan); 
    }

    /** * TRẢ SÁCH: Cập nhật trạng thái bản sao, cộng lại kho tổng, tính phạt quá hạn
     * ĐỒNG THỜI: Tự động chuyển trạng thái cọc thành REFUNDED nếu trước đó đã đặt cọc
     */
    @Transactional
    public void returnLoan(Long loanId) {
        Loan loan = findById(loanId);
        LocalDateTime now = LocalDateTime.now();

        for (LoanItem item : loan.getItems()) {
            if (!item.getReturned()) {
                item.setReturned(true);
                item.setReturnDate(now);
                
                BookCopy copy = item.getBookCopy();
                if (copy != null) {
                    // 1. Cập nhật trạng thái bản sao vật lý thành SẴN SÀNG
                    copy.setStatus(BookCopy.Status.AVAILABLE);
                    bookCopyRepository.save(copy);
                    
                    // 2. Cộng hoàn trả lại kho tổng (Book) để độc giả khác có thể mượn
                    Book book = copy.getBook();
                    if (book != null) {
                        int currentAvailable = book.getAvailableCopies() != null ? book.getAvailableCopies() : 0;
                        int currentTotal = book.getTotalCopies() != null ? book.getTotalCopies() : 0;
                        
                        // Đảm bảo an toàn không cộng vượt quá tổng số bản sao sở hữu
                        if (currentAvailable < currentTotal) {
                            book.setAvailableCopies(currentAvailable + 1);
                            bookRepository.save(book);
                        }
                    }
                }
            }
        }

        loan.setReturnDate(now);
        loan.setStatus(Status.RETURNED);

        // ─── THÊM VÀO: TỰ ĐỘNG CHUYỂN TRẠNG THÁI HOÀN CỌC KHI TRẢ SÁCH ───
        // Nếu phiếu mượn này trước đó đang ở trạng thái PAID (Đã cọc), tự động chuyển thành REFUNDED
        if (loan.getDepositStatus() == DepositStatus.PAID) {
            loan.setDepositStatus(DepositStatus.REFUNDED);
        }
        // ───────────────────────────────────────────────────────────────

        // Tính phạt nếu trả trễ hạn (Giữ nguyên logic gốc của bạn)
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
        if (daysLate > 0) {
            Fine fine = new Fine();
            fine.setLoan(loan);
            fine.setMember(loan.getMember());
            fine.setDaysOverdue((int) daysLate);
            fine.setFineAmount(fine.getFinePerDay().multiply(java.math.BigDecimal.valueOf(daysLate)));
            fine.setReason("Trả sách trễ " + daysLate + " ngày");
            fineRepository.save(fine);
        }

        loanRepository.save(loan);
    }

    /** * Cập nhật trạng thái OVERDUE cho tất cả các phiếu đã quá hạn 
     */
    @Transactional
    public void updateOverdueLoans() {
        List<Loan> activeLoans = loanRepository.findByStatus(Status.ACTIVE);
        LocalDate today = LocalDate.now();
        for (Loan loan : activeLoans) {
            if (loan.getDueDate().isBefore(today)) {
                loan.setStatus(Status.OVERDUE);
                loanRepository.save(loan);
            }
        }
    }

    @Transactional(readOnly = true)
    public long countByStatus(Status status) {
        return loanRepository.countByStatus(status);
    }

    /** * Lấy danh sách phiếu mượn mới nhất cấu hình cho Dashboard của Admin
     */
    @Transactional(readOnly = true)
    public List<LoanResponse> getRecentLoans(int limit) {
        List<Loan> loans = loanRepository.findRecentLoans(PageRequest.of(0, limit));

        return loans.stream().map(loan -> {
            LoanResponse<Object> res = new LoanResponse<>();
            res.setId(loan.getId());
            res.setLoanCode(loan.getLoanCode());
            
            if (loan.getMember() != null) {
                res.setMemberId(loan.getMember().getId());
                res.setMemberCardNumber(loan.getMember().getCardNumber());
                if (loan.getMember().getUser() != null) {
                    res.setMemberName(loan.getMember().getUser().getFullName());
                } else {
                    res.setMemberName("Độc giả chưa có tên");
                }
            } else {
                res.setMemberName("Độc giả ẩn danh");
            }

            res.setLoanDate(loan.getLoanDate());
            
            if (loan.getDueDate() != null) {
                res.setDueDate(loan.getDueDate().atStartOfDay());
            }

            res.setStatus(loan.getStatus() != null ? loan.getStatus().name() : "PENDING");
            res.setDepositPaid(loan.getDepositPaid());
            res.setNote(loan.getNote());
            
            return res;
        }).collect(Collectors.toList());
    }

    /** * ĐĂNG KÝ MƯỢN ONLINE (Client):
     * Tự động đặt trạng thái cọc ban đầu dựa vào giá trị deposit_amount của cuốn sách
     */
    @Transactional
    public Loan createOnlineLoan(Loan loan, Book book) {
        // 1. Kiểm tra số lượng tồn kho khả dụng
        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Rất tiếc, đầu sách này hiện tại đã hết quyển sẵn sàng!");
        }
        
        // 2. Trừ bớt 1 quyển sách khả dụng và ép đồng bộ xuống DB ngay
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Tạo một đối tượng User đại diện cho thủ thư hệ thống tự động duyệt tạm thời
        User systemLibrarian = new User();
        systemLibrarian.setId(1L); 
        loan.setLibrarian(systemLibrarian);

        // ─── THÊM VÀO: TỰ ĐỘNG THIẾT LẬP TRẠNG THÁI CỌC BAN ĐẦU KHI TẠO ĐƠN ONLINE ───
        // Nếu sách có yêu cầu tiền cọc lớn hơn 0, gắn trạng thái UNPAID (Chờ ra quầy đóng tiền)
        // Nếu sách không yêu cầu cọc (hoặc = 0), đặt trạng thái là NONE
        if (book.getDepositAmount() != null && book.getDepositAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            loan.setDepositStatus(DepositStatus.UNPAID);
        } else {
            loan.setDepositStatus(DepositStatus.NONE);
        }
        // ──────────────────────────────────────────────────────────────────────────

        // 3. Lưu thông tin gốc của Phiếu mượn
        Loan savedLoan = loanRepository.save(loan);

        // 4. Khởi tạo một bản sao vật lý ảo (BookCopy) để vượt qua ràng buộc dữ liệu
        BookCopy tempCopy = new BookCopy();
        tempCopy.setBook(book); 
        tempCopy.setStatus(BookCopy.Status.AVAILABLE); 
        tempCopy.setAcquiredDate(LocalDate.now()); 
        
        String autoBarcode = "BC-" + book.getId() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        tempCopy.setBarcode(autoBarcode);
        BookCopy savedCopy = bookCopyRepository.save(tempCopy);

        // 5. Liên kết chi tiết phiếu mượn (LoanItem) giữa Phiếu và Bản sao sách vừa tạo
        LoanItem item = new LoanItem();
        item.setLoan(savedLoan);        
        item.setBookCopy(savedCopy);    
        item.setReturned(false);
        loanItemRepository.save(item);  

        savedLoan.setItems(List.of(item));
        return savedLoan;
    }

    // ─── THÊM VÀO THÊM HÀM DUYỆT PHIẾU/XÁC NHẬN ĐÓNG TIỀN TẠI QUẦY (Dành cho Admin) ───
    /**
     * Xác nhận độc giả đã đóng tiền cọc tại quầy, chuyển trạng thái mượn và trạng thái cọc
     */
    @Transactional
    public void confirmDepositPaid(Long loanId) {
        Loan loan = findById(loanId);
        if (loan.getDepositStatus() == DepositStatus.UNPAID) {
            loan.setDepositStatus(DepositStatus.PAID); // Đã chuyển sang đóng tiền thành công
            loan.setStatus(Status.ACTIVE);             // Chuyển trạng thái phiếu từ CHỜ DUYỆT -> ĐANG MƯỢN
            loanRepository.save(loan);
        }
    }

    // Các phương thức mở rộng khác giữ nguyên
    @Transactional
    public void saveNew(Loan loan, Long memberId, Long librarianId, List<Long> copyIds) { }
    
    @Transactional
    public void update(Loan loan) { 
        loanRepository.save(loan); 
    }
    
    @Transactional
    public void deleteById(Long id) { 
        loanRepository.deleteById(id); 
    }
}