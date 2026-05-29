package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phone;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String studentCode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageStatus status = MessageStatus.UNREAD;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ Thêm mới: nội dung phản hồi từ admin
    @Column(columnDefinition = "TEXT")
    private String replyContent;

    // ✅ Thêm mới: thời điểm admin phản hồi
    private LocalDateTime repliedAt;

    public enum MessageStatus {
        UNREAD, READ, REPLIED
    }
}