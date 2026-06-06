package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thuvien.entity.ChatReply;
import java.util.List;

public interface ChatReplyRepository extends JpaRepository<ChatReply, Long> {
    // Lấy toàn bộ lịch sử tin nhắn của một liên hệ, sắp xếp theo thời gian
    List<ChatReply> findByContactMessageIdOrderByCreatedAtAsc(Long contactMessageId);
}