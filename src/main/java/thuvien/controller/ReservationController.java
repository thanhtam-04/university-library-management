package thuvien.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.Book;
import thuvien.entity.Member;
import thuvien.entity.Reservation;
import thuvien.repository.BookRepository;
import thuvien.repository.MemberRepository;
import thuvien.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/add/{bookId}")
    public String showReservationForm(@PathVariable Long bookId, Model model) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));
        
        if (book.getAvailableCopies() > 0) {
            return "redirect:/books/" + bookId;
        }
        
        model.addAttribute("book", book);
        return "reservation";
    }

    @PostMapping("/confirm/{bookId}")
    public String confirmReservation(@PathVariable Long bookId, 
                                     Authentication authentication,
                                     RedirectAttributes ra) {
        
        String username = authentication.getName();
        Member member = memberRepository.findByUserUsername(username).orElse(null); 

        if (member == null) {
            ra.addFlashAttribute("error", "Không tìm thấy thông tin thành viên!");
            return "redirect:/books/" + bookId;
        }
        
        Book book = bookRepository.findById(bookId).orElseThrow();

        // Kiểm tra chặn trùng: Chỉ cho phép đặt nếu chưa có đơn PENDING hoặc READY
        boolean exists = reservationRepository.existsByMemberAndBookAndStatusIn(
                member, book, List.of("PENDING", "READY"));
        
        if (exists) {
            ra.addFlashAttribute("error", "Bạn đã có đơn đặt trước cho cuốn sách này rồi!");
            return "redirect:/books/" + bookId;
        }

        // Tạo mới reservation
        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setMember(member);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStatus("PENDING");
        
        reservationRepository.save(reservation);

        ra.addFlashAttribute("successMsg", "Đặt lịch thành công! Chúng tôi sẽ thông báo cho bạn khi có sách.");
        return "redirect:/books/" + bookId;
    }
}