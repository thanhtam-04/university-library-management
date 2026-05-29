package thuvien.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.service.UserService;

/**
 * Chỉ xử lý tính năng DUYỆT tài khoản.
 * Route base: /admin/user  (KHÔNG có GET /list để tránh xung đột với AdminUserController)
 *
 * AdminUserController đã map:  GET /admin/user/list
 * Controller này map:          GET /admin/user/pending
 *                              POST /admin/user/approve/{id}
 *                              POST /admin/user/reject/{id}
 *                              POST /admin/user/toggle/{id}
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
}