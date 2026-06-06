package thuvien.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.ContactMessage;
import thuvien.repository.ChatReplyRepository; // Cần thêm import này
import thuvien.repository.ContactMessageRepository;
import thuvien.service.ContactService; // Cần thêm import này

@Controller
@RequestMapping("/admin/contact")
public class AdminContactController {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private ChatReplyRepository chatReplyRepository; // Để lấy lịch sử chat

    @Autowired
    private ContactService contactService; // Để gọi hàm addReply

    // 1. Danh sách liên hệ
    @GetMapping({"", "/", "/list"})
    public String listMessages(Model model) {
        model.addAttribute("messages", contactMessageRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("unreadCount", contactMessageRepository.countByStatus(ContactMessage.MessageStatus.UNREAD));
        model.addAttribute("activePage", "contact");
        return "views/admin/contact/list";
    }

    // 2. Mở giao diện Hội thoại (Thay thế cho Modal cũ)
    @GetMapping("/detail/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {
        ContactMessage msg = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy liên hệ"));
        
        model.addAttribute("contact", msg);
        // Lấy lịch sử chat không giới hạn
        model.addAttribute("messages", chatReplyRepository.findByContactMessageIdOrderByCreatedAtAsc(id));
        
        return "views/admin/contact/detail"; // Bạn cần tạo file này
    }

    // 3. Gửi phản hồi trong hội thoại
 // Trong AdminContactController.java

    @PostMapping("/send-reply")
    public String sendReply(@RequestParam Long messageId,
                            @RequestParam String replyContent,
                            RedirectAttributes ra) {
        
        // 1. Lưu phản hồi
        contactService.addReply(messageId, replyContent, "ADMIN", "Admin");
        
        // 2. Cập nhật trạng thái tin nhắn gốc thành REPLIED
        ContactMessage msg = contactMessageRepository.findById(messageId).orElseThrow();
        msg.setStatus(ContactMessage.MessageStatus.REPLIED); 
        contactMessageRepository.save(msg);
        
        ra.addFlashAttribute("successMsg", "Đã phản hồi thành công!");
        return "redirect:/admin/contact/detail/" + messageId;
    }

    // 4. Đánh dấu đã đọc
    @PostMapping("/read/{id}")
    public String markRead(@PathVariable Long id, RedirectAttributes ra) {
        contactMessageRepository.findById(id).ifPresent(msg -> {
            msg.setStatus(ContactMessage.MessageStatus.READ);
            contactMessageRepository.save(msg);
        });
        ra.addFlashAttribute("successMsg", "Đã đánh dấu đã đọc!");
        return "redirect:/admin/contact/list";
    }

    // 5. Xóa liên hệ
    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id, RedirectAttributes ra) {
        contactMessageRepository.deleteById(id);
        ra.addFlashAttribute("successMsg", "Đã xóa tin nhắn!");
        return "redirect:/admin/contact/list";
    }
}