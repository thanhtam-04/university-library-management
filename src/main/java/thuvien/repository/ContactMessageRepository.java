package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thuvien.entity.ContactMessage;

import java.util.List;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    List<ContactMessage> findAllByOrderByCreatedAtDesc();

    long countByStatus(ContactMessage.MessageStatus status);

    // ✅ Thêm mới: lấy lịch sử theo email (cho trang của user)
    List<ContactMessage> findByEmailOrderByCreatedAtDesc(String email);
}