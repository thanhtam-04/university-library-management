package thuvien.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.dto.response.BookResponse;
import thuvien.entity.Book;
import thuvien.entity.Loan;
import thuvien.entity.Member;
import thuvien.entity.User;
import thuvien.repository.MemberRepository;
import thuvien.service.BookService;
import thuvien.service.LoanService;
import thuvien.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller("userBookController") 
public class BookController {

    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;
    private final MemberRepository memberRepository;

    public BookController(BookService bookService, 
                          UserService userService, 
                          LoanService loanService, 
                          MemberRepository memberRepository) {
        this.bookService = bookService;
        this.userService = userService;
        this.loanService = loanService;
        this.memberRepository = memberRepository;
    }

    // ── 1. Xem Chi Tiết Đầu Sách ──
    @GetMapping("/books/detail/{id}")
    public String getBookDetail(@PathVariable("id") Long id, Model model, Authentication auth) {
        BookResponse bookResponse = bookService.getBookById(id);
        if (bookResponse == null) {
            return "redirect:/home";
        }
        
        model.addAttribute("book", bookResponse);
        
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            try {
                model.addAttribute("currentUser", userService.findByUsername(auth.getName()));
            } catch (Exception ignored) {}
        }
        
        return "book-detail"; 
    }

    // ── 2. Hiển Thị Trang Xác Nhận Gửi Phiếu Mượn (borrow.html) ──
    @GetMapping("/books/{id}/borrow")
    public String showBorrowConfirmation(@PathVariable("id") Long bookId, 
                                         Authentication auth, 
                                         Model model) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login"; 
        }

        BookResponse bookResponse = bookService.getBookById(bookId);
        if (bookResponse == null) {
            return "redirect:/home";
        }

        if (bookResponse.getAvailableCopies() <= 0) {
            model.addAttribute("error", "Đầu sách này hiện tại đã được mượn hết, không thể đăng ký trực tuyến!");
        }

        // SỬA: Lấy từ Entity gốc để đảm bảo có trường depositAmount vừa cập nhật trong Database
        Book bookEntity = bookService.findById(bookId);
        BigDecimal depositAmount = BigDecimal.ZERO;
        if (bookEntity != null) {
            depositAmount = bookEntity.getDepositAmount();
            if (depositAmount == null && bookEntity.getPrice() != null) {
                depositAmount = bookEntity.getPrice().multiply(new BigDecimal("0.70"));
            }
        }

        model.addAttribute("book", bookResponse);
        model.addAttribute("loanDate", LocalDateTime.now()); 
        model.addAttribute("dueDate", LocalDate.now().plusDays(14)); 
        model.addAttribute("calculatedDeposit", depositAmount != null ? depositAmount : BigDecimal.ZERO);

        try {
            model.addAttribute("currentUser", userService.findByUsername(auth.getName()));
        } catch (Exception ignored) {}

        return "borrow";
    }

 // ── 3. Xử Lý Tiếp Nhận Gửi Phiếu Mượn Xuống DB ──
    @PostMapping("/books/{id}/borrow-submit")
    public String processBorrowBook(@PathVariable("id") Long bookId,
                                    @RequestParam(value = "note", required = false) String note,
                                    @RequestParam(value = "isDepositPaidDirectly", required = false) Boolean isDepositPaidDirectly, // THÊM BIẾN NÀY ĐỂ HỨNG DỮ LIỆU CHECKBOX
                                    Authentication auth,
                                    Model model, 
                                    RedirectAttributes redirectAttributes) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(auth.getName());
        if (user == null) {
            return "redirect:/login";
        }

        Member member = memberRepository.findByUserId(user.getId()).orElse(null);
        if (member == null) {
            model.addAttribute("error", "Tài khoản của bạn chưa được liên kết với mã độc giả thư viện. Vui lòng liên hệ thủ thư!");
            model.addAttribute("book", bookService.getBookById(bookId));
            model.addAttribute("loanDate", LocalDateTime.now());
            model.addAttribute("dueDate", LocalDate.now().plusDays(14));
            model.addAttribute("calculatedDeposit", BigDecimal.ZERO);
            return "borrow";
        }

        Book bookEntity = bookService.findById(bookId); 
        if (bookEntity == null || bookEntity.getAvailableCopies() <= 0) {
            model.addAttribute("error", "Rất tiếc, đầu sách này hiện tại không khả dụng hoặc đã hết sách!");
            model.addAttribute("book", bookService.getBookById(bookId));
            model.addAttribute("loanDate", LocalDateTime.now());
            model.addAttribute("dueDate", LocalDate.now().plusDays(14));
            model.addAttribute("calculatedDeposit", BigDecimal.ZERO);
            return "borrow";
        }

        try {
            Loan loan = new Loan();
            String uniqueCode = "PM-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            loan.setLoanCode(uniqueCode);
            loan.setMember(member);
            loan.setNote(note);
            loan.setLoanDate(LocalDateTime.now());
            loan.setDueDate(LocalDate.now().plusDays(14)); 
            loan.setStatus(Loan.Status.PENDING); 

            // Tính toán số tiền cọc từ cấu hình sách
            BigDecimal depositAmount = bookEntity.getDepositAmount();
            if (depositAmount == null && bookEntity.getPrice() != null) {
                depositAmount = bookEntity.getPrice().multiply(new BigDecimal("0.70"));
            }
            
            BigDecimal finalDeposit = (depositAmount != null) ? depositAmount : BigDecimal.ZERO;
            loan.setDepositPaid(finalDeposit);

            // ── LOGIC XỬ LÝ ĐỒNG BỘ TRẠNG THÁI CỌC XUỐNG CƠ SỞ DỮ LIỆU ──
            if (finalDeposit.compareTo(BigDecimal.ZERO) <= 0) {
                // Nếu tiền cọc bằng 0 thì gán trạng thái MIỄN CỌC
                // Nhi hãy đổi 'NONE' thành tên Enum chính xác trong thuộc tính depositStatus của Entity Loan (ví dụ: Loan.DepositStatus.NONE)
                loan.setDepositStatus(Loan.DepositStatus.NONE); 
            } else {
                // Nếu tiền cọc > 0, kiểm tra xem nút tích chọn ở giao diện có được bật hay không
                if (isDepositPaidDirectly != null && isDepositPaidDirectly) {
                    loan.setDepositStatus(Loan.DepositStatus.PAID); // Đã cọc nếu được tích chọn
                } else {
                    loan.setDepositStatus(Loan.DepositStatus.UNPAID); // Chưa cọc nếu bỏ tích
                }
            }
            // ──────────────────────────────────────────────────────────

            // Gọi dịch vụ lưu thông tin đơn mượn vào hệ thống
            loanService.createOnlineLoan(loan, bookEntity);

            model.addAttribute("successMessage", "Yêu cầu đăng ký mượn sách với mã " + uniqueCode + " đã được gửi lên hệ thống thành công. Vui lòng chờ thủ thư phê duyệt!");
            
            model.addAttribute("book", bookService.getBookById(bookId));
            model.addAttribute("loanDate", loan.getLoanDate());
            model.addAttribute("dueDate", loan.getDueDate());
            model.addAttribute("calculatedDeposit", loan.getDepositPaid());
            model.addAttribute("currentUser", user);
            
            return "borrow"; 

        } catch (Exception e) {
            System.err.println("=== LỖI TẠO PHIẾU MƯỢN ONLINE: " + e.getMessage());
            model.addAttribute("error", "Không thể tạo yêu cầu mượn: " + e.getMessage());
            model.addAttribute("book", bookService.getBookById(bookId));
            model.addAttribute("loanDate", LocalDateTime.now());
            model.addAttribute("dueDate", LocalDate.now().plusDays(14));
            model.addAttribute("calculatedDeposit", BigDecimal.ZERO);
            return "borrow";
        }
    }
}