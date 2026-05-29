package thuvien.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thuvien.entity.ContactMessage;
import thuvien.repository.ContactMessageRepository;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/contact")
public class AdminContactController {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @GetMapping({"", "/", "/list"})
    public String listMessages(Model model) {
        model.addAttribute("messages", contactMessageRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("unreadCount", contactMessageRepository.countByStatus(ContactMessage.MessageStatus.UNREAD));
        model.addAttribute("activePage", "contact");
        return "views/admin/contact/list";
    }

    @PostMapping("/read/{id}")
    public String markRead(@PathVariable Long id, RedirectAttributes ra) {
        contactMessageRepository.findById(id).ifPresent(msg -> {
            msg.setStatus(ContactMessage.MessageStatus.READ);
            contactMessageRepository.save(msg);
        });
        ra.addFlashAttribute("successMsg", "Đã đánh dấu đã đọc!");
        return "redirect:/admin/contact/list";
    }

    // ✅ Lưu replyContent + repliedAt vào database
    @PostMapping("/send-reply")
    public String sendReply(@RequestParam Long messageId,
                            @RequestParam String replyContent,
                            RedirectAttributes ra) {
        
        contactMessageRepository.findById(messageId).ifPresent(msg -> {
            msg.setStatus(ContactMessage.MessageStatus.REPLIED);
            msg.setReplyContent(replyContent);           // ← Quan trọng nhất
            msg.setRepliedAt(LocalDateTime.now());       // ← Ghi thời gian phản hồi
            contactMessageRepository.save(msg);
        });
        
        ra.addFlashAttribute("successMsg", "Đã gửi phản hồi thành công cho người dùng!");
        return "redirect:/admin/contact/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id, RedirectAttributes ra) {
        contactMessageRepository.deleteById(id);
        ra.addFlashAttribute("successMsg", "Đã xóa tin nhắn!");
        return "redirect:/admin/contact/list";
    }
}