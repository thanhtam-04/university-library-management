package thuvien.controller;

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
import thuvien.repository.ChatReplyRepository;
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
    private ChatReplyRepository chatReplyRepository; // Đã thêm để lấy lịch sử chat

    @Autowired
    private UserService userService;

    /* ─── Trang liên hệ công khai ───────────────────────── */
    @GetMapping("/contact")
    public String contactPage(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
            
            // Truy vấn tất cả yêu cầu của User này
            var messages = contactMessageRepository.findByEmailOrderByCreatedAtDesc(currentUser.getEmail());
            
            if (!messages.isEmpty()) {
                // Lấy yêu cầu mới nhất để hiện khung chat
                ContactMessage latest = messages.get(0);
                model.addAttribute("contact", latest);
                // Lấy tất cả phản hồi của yêu cầu này
                model.addAttribute("messages", chatReplyRepository.findByContactMessageIdOrderByCreatedAtAsc(latest.getId()));
            }
        }
        return "contact"; 
    }
    @PostMapping("/contact")
    public String handleContactSubmit(@ModelAttribute ContactRequest contactRequest,
                                      RedirectAttributes redirectAttributes) {
        try {
            contactService.saveContact(contactRequest); // Dùng service cho gọn
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

        // 1. Lấy danh sách yêu cầu của user
        var messages = contactMessageRepository.findByEmailOrderByCreatedAtDesc(user.getEmail());
        
        // 2. Lấy tất cả reply của các tin nhắn này (để hiển thị lịch sử chat)
        // Giả sử bạn muốn lấy tất cả reply của các tin nhắn này để hiển thị chung
        var allReplies = chatReplyRepository.findAll(); 
        
        model.addAttribute("messages", messages);
        model.addAttribute("chatReplies", allReplies);
        model.addAttribute("currentUser", user);
        return "phanHoi";
    }

    /* ─── CHỨC NĂNG CHAT HỘI THOẠI MỚI ─────────────────── */
    
    // 1. Mở giao diện chat chi tiết
    @GetMapping("/contact/detail/{id}")
    public String viewChatDetail(@PathVariable Long id, Model model) {
        ContactMessage contact = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy liên hệ"));
        
        model.addAttribute("contact", contact);
        // Lấy lịch sử chat không giới hạn
        model.addAttribute("messages", chatReplyRepository.findByContactMessageIdOrderByCreatedAtAsc(id));
        
        return "contact-detail"; 
    }

    // 2. Gửi phản hồi trong hội thoại
 // Sửa trong ContactController.java
    @PostMapping("/contact/reply")
    public String sendReply(@RequestParam Long id, 
                            @RequestParam String content,
                            @RequestParam String role) { 
        
        // Lưu phản hồi vào DB
        contactService.addReply(id, content, role, "Người dùng/Admin");
        
        // Redirect về trang chủ contact, Controller sẽ tự fetch lại dữ liệu mới nhất
        return "redirect:/contact"; 
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