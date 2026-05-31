package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.config.CustomUserDetails;
import thuvien.entity.User;
import thuvien.service.UserService;

@Controller
@RequestMapping("/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final UserService userService;

    // 1. Hiển thị trang Hồ sơ cá nhân
    @GetMapping
    public String profile(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        // Lấy thông tin user từ database dựa trên username trong phiên đăng nhập
        User user = userService.findByUsername(customUserDetails.getUsername());
        
        model.addAttribute("user", user);
        model.addAttribute("activePage", "profile"); // Để highlight menu nếu cần
        return "views/admin/profile";
    }

    // 2. Xử lý lưu thông tin hồ sơ
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User user, RedirectAttributes ra) {
        try {
            userService.updateProfile(user);
            ra.addFlashAttribute("successMsg", "Cập nhật hồ sơ thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }
}