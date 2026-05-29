package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thuvien.dto.request.UserRequest;
import thuvien.entity.Role;
import thuvien.entity.User;
import thuvien.exception.BadRequestException;
import thuvien.repository.MemberRepository;
import thuvien.repository.RoleRepository;
import thuvien.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository   userRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository   roleRepository;
    private final PasswordEncoder  passwordEncoder;

    /* ══════════════════════════════
        READ (XEM DỮ LIỆU)
    ══════════════════════════════ */

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user id=" + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy username: " + username));
    }

    /** Danh sách tài khoản chờ duyệt (isApproved = false) */
    public List<User> findPendingUsers() {
        return userRepository.findByIsApprovedFalse();
    }

    /** Danh sách tài khoản đã được duyệt (isApproved = true) */
    public List<User> findApprovedUsers() {
        return userRepository.findByIsApprovedTrue();
    }

    /** Số lượng tài khoản chờ duyệt — dùng cho badge sidebar */
    public long countPending() {
        return userRepository.countByIsApprovedFalse();
    }

    /** Danh sách User chưa được liên kết với Member nào */
    public List<User> findUsersWithoutMember() {
        List<Long> usedIds = memberRepository.findAll()
                .stream()
                .filter(m -> m.getUser() != null)
                .map(m -> m.getUser().getId())
                .toList();

        return userRepository.findAll()
                .stream()
                .filter(u -> !usedIds.contains(u.getId()))
                .toList();
    }

    /* ══════════════════════════════
        WRITE – CRUD
    ══════════════════════════════ */

    @Transactional
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        User existing = findById(user.getId());
        existing.setFullName(user.getFullName());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        existing.setAvatar(user.getAvatar());
        existing.setIsActive(user.getIsActive());

        if (user.getPassword() != null 
                && !user.getPassword().isBlank() 
                && !user.getPassword().startsWith("$2a$")) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /* ══════════════════════════════
        WRITE – ĐĂNG KÝ & DUYỆT
    ══════════════════════════════ */

    /**
     * Đăng ký tài khoản mới.
     * Logic: Chỉ tạo User, KHÔNG tạo Member tự động. 
     * Thành viên chỉ được tạo khi Admin nhấn nút Duyệt.
     */
    @Transactional
    public User registerUser(UserRequest request) {
        if (existsByUsername(request.getUsername())) {
            throw new BadRequestException("Tên đăng nhập '" + request.getUsername() + "' đã tồn tại");
        }
        if (request.getEmail() != null && existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email '" + request.getEmail() + "' đã được sử dụng");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setIsActive(true);         // Tài khoản kích hoạt để có thể đăng nhập (tùy logic Nhi)
        user.setIsApproved(false);      // Trạng thái chờ Admin duyệt

        // Gán quyền Sinh viên mặc định
        Role studentRole = roleRepository
                .findByName(Role.RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Role STUDENT"));
        user.getRoles().add(studentRole);

        return userRepository.save(user);
    }

    /**
     * Cập nhật trạng thái duyệt nhanh từ giao diện
     */
    @Transactional
    public void approveUser(Long userId, boolean approve) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng có ID: " + userId));
        
        // Gọi câu Query cập nhật trạng thái trong UserRepository
        userRepository.updateApprovalStatus(userId, approve);
    }

    @Transactional
    public void toggleActive(Long id) {
        User user = findById(id);
        user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
        userRepository.save(user);
    }

    @Transactional
    public void resetPassword(Long id, String newRawPassword) {
        User user = findById(id);
        user.setPassword(passwordEncoder.encode(newRawPassword));
        user.save(user);
    }

    /* ══════════════════════════════
        UTIL (TIỆN ÍCH)
    ══════════════════════════════ */

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

	public void approve(Long id) {
		// TODO Auto-generated method stub
		
	}

	public void reject(Long id) {
		// TODO Auto-generated method stub
		
	}
}