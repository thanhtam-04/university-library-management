package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.User; // Đã sửa lại import chuẩn Entity User của dự án
import thuvien.service.UserService;

/**
 * Xử lý tính năng duyệt tài khoản và cập nhật quyền hạn nhân sự.
 */
@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* ────────────────────────────────────────────
       GET /admin/user/pending
       Danh sách tài khoản chờ duyệt
    ──────────────────────────────────────────── */
    @GetMapping("/pending")
    public String pending(Model model) {
        model.addAttribute("pendingUsers",  userService.findPendingUsers());
        model.addAttribute("pendingCount",  userService.countPending());
        model.addAttribute("activePage", "user");
        return "views/user/pending";            // templates/views/user/pending.html
    }

    /* ────────────────────────────────────────────
       POST /admin/user/approve/{id}
       Admin duyệt tài khoản → isApproved = true
    ──────────────────────────────────────────── */
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userService.approve(id);
            ra.addFlashAttribute("successMsg", "✅ Đã duyệt tài khoản thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "❌ Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/user/pending";
    }

    /* ────────────────────────────────────────────
       POST /admin/user/reject/{id}
       Admin từ chối → xóa tài khoản
    ──────────────────────────────────────────── */
    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userService.reject(id);
            ra.addFlashAttribute("successMsg", "🗑 Đã từ chối và xóa yêu cầu đăng ký.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "❌ Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/user/pending";
    }

    /* ────────────────────────────────────────────
       POST /admin/user/toggle/{id}
       Bật / tắt khoá tài khoản đã duyệt
    ──────────────────────────────────────────── */
    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userService.toggleActive(id);
            ra.addFlashAttribute("successMsg", "Đã cập nhật trạng thái tài khoản.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "❌ Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/user/list";     // về trang list của AdminUserController
    }

    /* ────────────────────────────────────────────
       POST /admin/user/update-role
       Đã đổi URL để tránh trùng lặp hoàn toàn với AdminUserController
    ──────────────────────────────────────────── */
    @PostMapping("/update-role")
    public String updateUser(@ModelAttribute("user") User user, 
                             @RequestParam("roleIds") Long roleId, 
                             RedirectAttributes ra) {
        try {
            // Đã nhận đúng Entity thuvien.entity.User
            userService.update(user, roleId);
            ra.addFlashAttribute("successMsg", "✅ Cập nhật thông tin và vai trò tài khoản thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "❌ Lỗi cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/user/list";
    }
    
}