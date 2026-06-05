package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.*;
import thuvien.entity.Loan.Status;
import thuvien.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller("realAdminLoanController")
@RequestMapping("/admin/loan") 
@RequiredArgsConstructor
public class LoanController {
    private final ReservationRepository reservationRepository;
    private final LoanRepository       loanRepository;
    private final BookCopyRepository   bookCopyRepository;
    private final FineRepository       fineRepository;
    private final BookRepository       bookRepository;

    /**
     * 1. DANH SÁCH PHIẾU MƯỢN
     */
    @GetMapping("/list") 
    public String listLoans(Model model) {
        List<Loan> list = loanRepository.findAll();
        model.addAttribute("loans", list);
        return "views/admin/loan/list"; 
    }

    /**
     * GIAO DIỆN THÊM MỚI PHIẾU MƯỢN
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("loan", new Loan());
        return "views/admin/loan/add";
    }

    /**
     * GIAO DIỆN CHỈNH SỬA PHIẾU MƯỢN
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn ID: " + id));
        model.addAttribute("loan", loan);
        return "views/admin/loan/edit";
    }

    /**
     * 2. XỬ LÝ PHÊ DUYỆT PHIẾU MƯỢN ONLINE - ĐỒNG BỘ TRẠNG THÁI CỌC
     */
    @GetMapping("/approve/{id}")
    @Transactional
    public String approveLoan(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Loan loan = loanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn ID: " + id));
            
            // Chuyển trạng thái sang đang mượn
            loan.setStatus(Status.ACTIVE);
            
            // ĐỒNG BỘ TIỀN CỌC: Khi thủ thư bấm duyệt nghĩa là thủ thư đã xác nhận thu tiền cọc (Nếu có)
            if (loan.getDepositStatus() == Loan.DepositStatus.UNPAID) {
                loan.setDepositStatus(Loan.DepositStatus.PAID);
            }
            
            if (loan.getItems() != null) {
                for (LoanItem item : loan.getItems()) {
                    BookCopy copy = item.getBookCopy();
                    if (copy != null) {
                        copy.setStatus(BookCopy.Status.BORROWED);
                        bookCopyRepository.save(copy);
                        
                        Book book = copy.getBook();
                        if (book != null) {
                            int currentAvailable = (book.getAvailableCopies() != null) ? book.getAvailableCopies() : 0;
                            book.setAvailableCopies(Math.max(0, currentAvailable - 1));
                            bookRepository.save(book);
                        }
                    }
                }
            }
            
            loanRepository.save(loan);
            redirectAttributes.addFlashAttribute("successMsg", "Đã phê duyệt và cập nhật trạng thái đặt cọc thành công!");
        } catch (Exception e) {
            e.printStackTrace(); 
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi phê duyệt đơn: " + e.getMessage());
        }
        return "redirect:/admin/loan/list"; 
    }

    /**
     * 3. XỬ LÝ TRẢ SÁCH VÀ TỰ ĐỘNG HOÀN CỌC
     */
    @GetMapping("/return/{id}")
    @Transactional
    public String returnLoan(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Loan loan = loanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn ID: " + id));
            LocalDateTime now = LocalDateTime.now();

            if (loan.getItems() != null) {
                for (LoanItem item : loan.getItems()) {
                    if (item != null && !item.getReturned()) {
                        item.setReturned(true);
                        item.setReturnDate(now);
                        
                        BookCopy copy = item.getBookCopy();
                        if (copy != null) {
                            copy.setStatus(BookCopy.Status.AVAILABLE); 
                            bookCopyRepository.save(copy);
                            
                            Book book = copy.getBook();
                            if (book != null) {
                                Reservation next = reservationRepository.findFirstByBookAndStatusOrderByReservationDateAsc(book, "PENDING");
                                if (next != null) {
                                    next.setStatus("READY");
                                    reservationRepository.save(next);
                                } else {
                                    int current = (book.getAvailableCopies() != null) ? book.getAvailableCopies() : 0;
                                    book.setAvailableCopies(current + 1);
                                    bookRepository.save(book);
                                }
                            }
                        }
                    }
                }
            }

            loan.setReturnDate(now);
            loan.setStatus(Status.RETURNED);

            // TỰ ĐỘNG HOÀN TRẢ TIỀN CỌC CHO ĐỘC GIẢ
            if (loan.getDepositStatus() == Loan.DepositStatus.PAID) {
                loan.setDepositStatus(Loan.DepositStatus.REFUNDED);
            }

            // Xử lý phạt quá hạn
            if (loan.getDueDate() != null && LocalDate.now().isAfter(loan.getDueDate())) {
                long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
                if (daysLate > 0) {
                    Fine fine = new Fine();
                    fine.setLoan(loan);
                    fine.setMember(loan.getMember());
                    fine.setDaysOverdue((int) daysLate);
                    BigDecimal finePerDay = (fine.getFinePerDay() != null) ? fine.getFinePerDay() : new BigDecimal("5000");
                    fine.setFinePerDay(finePerDay);
                    fine.setFineAmount(finePerDay.multiply(BigDecimal.valueOf(daysLate)));
                    fine.setReason("Trả sách trễ " + daysLate + " ngày");
                    fineRepository.save(fine);
                }
            }

            loanRepository.save(loan);
            redirectAttributes.addFlashAttribute("successMsg", "Đã trả sách và hoàn cọc thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi xử lý trả sách: " + e.getMessage());
        }
        return "redirect:/admin/loan/list";
    }

    /**
     * 4. XÓA PHIẾU MƯỢN
     */
    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Loan loan = loanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn ID: " + id));
            
            if (loan.getItems() != null) {
                for (LoanItem item : loan.getItems()) {
                    if (item != null) {
                        BookCopy copy = item.getBookCopy();
                        if (copy != null) {
                            copy.setStatus(BookCopy.Status.AVAILABLE);
                            bookCopyRepository.save(copy);
                        }
                    }
                }
            }
            
            loanRepository.delete(loan);
            redirectAttributes.addFlashAttribute("successMsg", "Xóa thành công phiếu mượn!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/loan/list";
    }
}