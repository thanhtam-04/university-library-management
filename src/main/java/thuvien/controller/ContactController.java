package thuvien.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.dto.request.ContactRequest;
import thuvien.entity.ContactMessage;
import thuvien.entity.User;
import thuvien.repository.ContactMessageRepository;
import thuvien.service.ContactService;
import thuvien.service.UserService;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private UserService userService;

    /* ─── Trang liên hệ công khai ───────────────────────── */
    @GetMapping("/contact")
    public String contactPage(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);   // ← Phải có dòng này
        }
        return "contact";
    }

    @PostMapping("/contact")
    public String handleContactSubmit(@ModelAttribute ContactRequest contactRequest,
                                      RedirectAttributes redirectAttributes) {
        try {
            ContactMessage message = ContactMessage.builder()
                    .fullName(contactRequest.getFullName())
                    .email(contactRequest.getEmail())
                    .phone(contactRequest.getPhone())
                    .subject(contactRequest.getSubject())
                    .message(contactRequest.getMessage())
                    .studentCode(contactRequest.getStudentCode())
                    .status(ContactMessage.MessageStatus.UNREAD)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();
            contactMessageRepository.save(message);
            redirectAttributes.addFlashAttribute("successMsg", "Tin nhắn của bạn đã được gửi thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi, vui lòng thử lại sau!");
        }
        return "redirect:/contact";
    }

    /* ─── Lịch sử liên hệ (yêu cầu đăng nhập) ──────────── */
    @GetMapping("/phanHoi")
    public String history(Model model) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";

        var messages = contactMessageRepository
                .findByEmailOrderByCreatedAtDesc(user.getEmail());

        model.addAttribute("messages",    messages);
        model.addAttribute("currentUser", user);
        model.addAttribute("user",        user);
        return "phanHoi";  // templates/phanHoi.html
    }

    /* ─── Gửi tin nhắn mới từ trang lịch sử ─────────────── */
    @PostMapping("/contact/send")
    public String send(@RequestParam String subject,
                       @RequestParam String message,
                       RedirectAttributes ra) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";

        ContactMessage msg = ContactMessage.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .subject(subject)
                .message(message)
                .status(ContactMessage.MessageStatus.UNREAD)
                .build();
        contactMessageRepository.save(msg);
        ra.addFlashAttribute("successMsg", "Đã gửi tin nhắn! Admin sẽ phản hồi sớm nhất có thể.");
        return "redirect:/contact/history";
    }

    /* ─── Helper ─────────────────────────────────────────── */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) return null;
        try { return userService.findByUsername(auth.getName()); }
        catch (Exception e) { return null; }
    }
}