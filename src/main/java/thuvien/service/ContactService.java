package thuvien.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thuvien.entity.ContactMessage;
import thuvien.entity.ChatReply;
import thuvien.repository.ContactMessageRepository;
import thuvien.repository.ChatReplyRepository;
import thuvien.dto.request.ContactRequest;

import java.time.LocalDateTime;

@Service
public class ContactService {

    @Autowired
    private ContactMessageRepository repository;

    @Autowired
    private ChatReplyRepository chatReplyRepository;

    // 1. Lưu yêu cầu liên hệ ban đầu
    public void saveContact(ContactRequest request) {
        ContactMessage msg = ContactMessage.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .subject(request.getSubject())
                .message(request.getMessage())
                .studentCode(request.getStudentCode())
                .status(ContactMessage.MessageStatus.UNREAD)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(msg);
    }

    // 2. Logic lưu tin nhắn phản hồi mới (Dùng cho cả Admin và User)
    public void addReply(Long contactId, String content, String senderRole, String senderName) {
        // Tìm ticket gốc
        ContactMessage contactMessage = repository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu liên hệ"));

        // Tạo tin nhắn mới
        ChatReply reply = ChatReply.builder()
                .contactMessage(contactMessage)
                .content(content)
                .senderRole(senderRole)
                .createdAt(LocalDateTime.now())
                .build();
        
        chatReplyRepository.save(reply);

        // Cập nhật trạng thái của ticket (nếu cần)
        if ("ADMIN".equals(senderRole)) {
            contactMessage.setStatus(ContactMessage.MessageStatus.REPLIED);
            repository.save(contactMessage);
        }
    }
}