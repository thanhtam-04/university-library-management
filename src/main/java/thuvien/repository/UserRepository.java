package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import thuvien.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ── Tìm kiếm cơ bản ──────────────────────────────────────────────
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // ── Theo trạng thái duyệt ─────────────────────────────────────────
    /** Danh sách tài khoản chờ duyệt (isApproved = false) */
    List<User> findByIsApprovedFalse();

    /** Danh sách tài khoản đã được duyệt (isApproved = true) */
    List<User> findByIsApprovedTrue();

    /** Đếm số tài khoản chờ duyệt — dùng cho badge trên sidebar */
    long countByIsApprovedFalse();

    // ── Theo trạng thái hoạt động ─────────────────────────────────────
    /** Danh sách tài khoản đang hoạt động */
    List<User> findByIsActiveTrue();

    /** Danh sách tài khoản bị khoá */
    List<User> findByIsActiveFalse();

    // ── HÀM ĐƯỢC THÊM MỚI: Cập nhật trạng thái duyệt nhanh bằng Query ──
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isApproved = :isApproved WHERE u.id = :id")
    void updateApprovalStatus(Long id, Boolean isApproved);
 // ── 🛠️ THAY THẾ HÀM CŨ BẰNG ĐOẠN NÀY ──
    // Câu lệnh này sẽ quét vào bên trong danh sách roles của User và lọc theo tên quyền
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name IN :roleNames")
    List<User> findByRolesIn(List<String> roleNames);
}