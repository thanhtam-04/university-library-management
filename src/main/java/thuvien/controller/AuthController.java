package thuvien.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thuvien.dto.request.UserRequest;
import thuvien.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ── LOGIN ──────────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        } else if (logout != null) {
            model.addAttribute("success", "Bạn đã đăng xuất thành công.");
        }
        // Bỏ đoạn else cũ đi để mặc định không hiện lỗi khi mới vào trang

        return "auth/login";
    }

    // ── REGISTER ───────────────────────────────────────────
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "auth/register";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("error", "Bạn không có quyền truy cập trang này!");
        return "auth/login";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("userRequest") UserRequest userRequest,
            Model model) {
        try {
            // Đăng ký tài khoản qua Service. 
            // Bên trong Service sẽ xử lý: lưu User với isApproved = false và CHƯA tạo Member.
            userService.registerUser(userRequest);
            
            model.addAttribute("success", "Đăng ký thành công! Vui lòng đợi quản trị viên phê duyệt tài khoản.");
            return "auth/login"; 
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("userRequest", userRequest);
            return "auth/register"; 
        }
    }

    // ── ROOT redirect ──────────────────────────────────────
    @GetMapping("/auth-root")
    public String root() {
        return "redirect:/admin/dashboard";
    }
}