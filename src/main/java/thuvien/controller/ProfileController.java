package thuvien.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.Loan;
import thuvien.entity.User;
import thuvien.repository.MemberRepository;
import thuvien.service.LoanService;
import thuvien.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService      userService;
    private final LoanService      loanService;
    private final MemberRepository memberRepository;

    /* ─── GET /profile ──────────────────────────────────── */
    @GetMapping
    public String profile(Model model) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";

        model.addAttribute("user",        user);
        model.addAttribute("currentUser", user);   // dùng trong navbar

        // Thẻ thành viên
        memberRepository.findByUserId(user.getId())
                .ifPresent(m -> model.addAttribute("member", m));

        // 5 lần mượn gần nhất
        memberRepository.findByUserId(user.getId()).ifPresent(m -> {
            try {
                List<Loan> recent = loanService.findByMember(m.getId());
                model.addAttribute("recentLoans",
                        recent.stream().limit(5).toList());
            } catch (Exception ignored) {}
        });

        return "profile";   // templates/profile.html
    }

    /* ─── POST /profile/update ──────────────────────────── */
    @PostMapping("/update")
    public String update(@RequestParam String fullName,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         RedirectAttributes ra) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        userService.save(user);

        ra.addFlashAttribute("successMsg", "✅ Cập nhật thông tin thành công!");
        return "redirect:/profile";
    }

    /* ─── POST /profile/change-password ─────────────────── */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes ra) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";

        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("errorMsg", "❌ Mật khẩu xác nhận không khớp!");
            return "redirect:/profile";
        }
        if (newPassword.length() < 6) {
            ra.addFlashAttribute("errorMsg", "❌ Mật khẩu mới phải ít nhất 6 ký tự!");
            return "redirect:/profile";
        }

        try {
            userService.resetPassword(user.getId(), newPassword);
            ra.addFlashAttribute("successMsg", "✅ Đổi mật khẩu thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "❌ Lỗi: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    /* ─── helper ─────────────────────────────────────────── */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) return null;
        try { return userService.findByUsername(auth.getName()); }
        catch (Exception e) { return null; }
    }
}