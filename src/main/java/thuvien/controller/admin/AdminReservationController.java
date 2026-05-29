package thuvien.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.Reservation;
import thuvien.repository.ReservationRepository;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/reservation")
public class AdminReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/list")
    public String listAllReservations(Model model) {
        model.addAttribute("reservations", reservationRepository.findAll());
        return "views/admin/reservation/list";
    }

    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable Long id, 
                               @RequestParam String status, 
                               RedirectAttributes ra) {
        
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn: " + id));

        // LOGIC MỚI: Kiểm tra điều kiện trước khi cho phép trạng thái READY
        if ("READY".equals(status)) {
            if (reservation.getBook().getAvailableCopies() <= 0) {
                ra.addFlashAttribute("errorMsg", "Không thể chuyển sang Sẵn sàng: Sách hiện chưa có trong kho!");
                return "redirect:/admin/reservation/list";
            }
            // Ghi lại thời điểm thông báo
            reservation.setNotifiedDate(LocalDateTime.now());
        }

        reservation.setStatus(status);
        reservationRepository.save(reservation);

        ra.addFlashAttribute("successMsg", "Đã cập nhật trạng thái đơn #" + id + " thành: " + status);
        return "redirect:/admin/reservation/list";
    }
}