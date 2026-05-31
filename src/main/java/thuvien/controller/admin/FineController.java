package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.Fine;
import thuvien.entity.Fine.Status;
import thuvien.entity.User;
import thuvien.repository.UserRepository;
import thuvien.service.FineService;
import java.util.List;

@Controller
@RequestMapping("/admin/fine")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;
    private final UserRepository userRepository;

 // ─── DANH SÁCH TẤT CẢ ────────────────────────────────────
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String status,
            Model model) {

        // KÍCH HOẠT: Tự động đồng bộ các phiếu mượn quá hạn vào bảng phí phạt
        fineService.syncOverdueLoans();

        List<Fine> list;
        if (status != null && !status.isBlank()) {
            try {
                list = fineService.findByStatus(Status.valueOf(status.toUpperCase()));
                model.addAttribute("filterStatus", status.toUpperCase());
            } catch (IllegalArgumentException e) {
                list = fineService.findAll();
            }
        } else {
            list = fineService.findAll();
        }

        // Thống kê nhanh
        long totalUnpaid  = list.stream().filter(f -> f.getStatus() == Status.UNPAID).count();
        long totalPaid    = list.stream().filter(f -> f.getStatus() == Status.PAID).count();
        long totalWaived  = list.stream().filter(f -> f.getStatus() == Status.WAIVED).count();

        model.addAttribute("list",        list);
        model.addAttribute("totalUnpaid", totalUnpaid);
        model.addAttribute("totalPaid",   totalPaid);
        model.addAttribute("totalWaived", totalWaived);
        model.addAttribute("activePage",  "fine");
        return "views/admin/fine/list";
    }

    // ─── CHI TIẾT ─────────────────────────────────────────────
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Fine fine = fineService.findById(id);
        model.addAttribute("fine",       fine);
        model.addAttribute("activePage", "fine");
        return "views/admin/fine/detail";
    }

    // ─── THANH TOÁN ───────────────────────────────────────────
 // ─── THANH TOÁN ───────────────────────────────────────────
    @PostMapping("/pay/{id}")
    public String pay(@PathVariable Long id,
                      @RequestParam(value = "paymentMethod", defaultValue = "CASH") String paymentMethod,
                      @AuthenticationPrincipal UserDetails userDetails,
                      RedirectAttributes ra) {
        try {
            User operator = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng ban quản trị"));
            
            // Truyền thêm phương thức thanh toán xuống tầng nghiệp vụ
            fineService.markAsPaid(id, operator, paymentMethod);
            ra.addFlashAttribute("successMsg", "Thanh toán và tạo hóa đơn thu phí phạt thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi xử lý thanh toán: " + e.getMessage());
        }
        return "redirect:/admin/fine/list";
    }

    // ─── MIỄN GIẢM ────────────────────────────────────────────
    @PostMapping("/waive/{id}")
    public String waive(@PathVariable Long id, RedirectAttributes ra) {
        try {
            fineService.waive(id);
            ra.addFlashAttribute("successMsg", "Đã miễn giảm phí phạt!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/fine/list";
    }
    
}