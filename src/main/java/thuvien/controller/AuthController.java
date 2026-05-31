package thuvien.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thuvien.dto.request.UserRequest;
import thuvien.entity.User;
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
            HttpSession session, // Bổ sung để hiển thị thông báo từ bộ chặn Interceptor nếu có
            Model model) {

        if (error != null) {
            if (error.equals("timeout")) {
                model.addAttribute("error", "Phiên đăng nhập đã hết hạn hoặc bạn chưa đăng nhập!");
            } else {
                model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        } else if (logout != null) {
            model.addAttribute("success", "Bạn đã đăng xuất thành công.");
        }

        // Kiểm tra xem có thông báo lỗi từ bộ chặn Interceptor chuyển qua không
        String errorMsg = (String) session.getAttribute("errorMsg");
        if (errorMsg != null) {
            model.addAttribute("error", errorMsg);
            session.removeAttribute("errorMsg"); // Hiển thị xong thì xóa đi để không bị lặp lại
        }

        return "auth/login";
    }

    /**
     * XỬ LÝ LOGIC ĐĂNG NHẬP VÀ LƯU USER VÀO SESSION
     */
    @PostMapping("/login")
    public String loginUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        try {
            // Gọi Service để kiểm tra tài khoản, mật khẩu, trạng thái duyệt (isApproved) và trạng thái hoạt động (isActive)
            User user = userService.login(username, password);
            
            // Đăng nhập thành công -> Lưu thông tin User vào Session để bộ lọc AuthInterceptor nhận diện quyền
            session.setAttribute("currentUser", user);
            
            // Điều hướng dựa theo vai trò (Role) của người dùng sau khi đăng nhập thành công
         // Kiểm tra danh sách quyền trực tiếp tại Controller để không bị lỗi build Lombok
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(r -> r.getName() == thuvien.entity.Role.RoleName.ROLE_ADMIN);
            boolean isLibrarian = user.getRoles().stream()
                    .anyMatch(r -> r.getName() == thuvien.entity.Role.RoleName.ROLE_LIBRARIAN);

            // Điều hướng dựa theo vai trò (Role)
            if (isAdmin || isLibrarian) {
                return "redirect:/admin/dashboard"; // Admin và Thủ thư vào trang quản trị
            } else {
                return "redirect:/home"; // Sinh viên (Student) về giao diện tra cứu sách của Client
            }
            
        } catch (Exception e) {
            // Đăng nhập thất bại (Sai mật khẩu, tài khoản chưa được duyệt...) -> Trả lại trang login kèm thông báo lỗi
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    // ── LOGOUT ─────────────────────────────────────────────
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Hủy toàn bộ thông tin trong session khi người dùng đăng xuất
        session.invalidate();
        return "redirect:/login?logout";
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
        return "redirect:/admin/book/list";
    }
    @GetMapping("/admin/403")
    public String accessDeniedPage() {
        return "auth/403"; // Trả về đường dẫn chứa file 403.html của Nhi
    }
}