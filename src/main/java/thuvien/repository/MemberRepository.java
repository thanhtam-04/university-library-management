package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thuvien.entity.Member;
import thuvien.entity.Member.Status;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByCardNumber(String cardNumber);
    Optional<Member> findByStudentCode(String studentCode);
    Optional<Member> findByUserId(Long userId);
    List<Member> findByStatus(Status status);
    
    // ✅ SỬA: Tìm kiếm và kiểm tra email "xuyên qua" thực thể User liên kết
    boolean existsByUserEmail(String email);
    Optional<Member> findByUserEmail(String email);
    Optional<Member> findByUserUsername(String username);
}