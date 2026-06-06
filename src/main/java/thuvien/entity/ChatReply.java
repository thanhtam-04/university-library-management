package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_replies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với yêu cầu liên hệ gốc (ContactMessage)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_message_id")
    private ContactMessage contactMessage;

    @Column(columnDefinition = "TEXT")
    private String content;

    // Phân biệt ai gửi: "USER" hoặc "ADMIN"
    private String senderRole; 

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}