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
	/* ══════════════════════════════
    LOGIC XỬ LÝ ĐĂNG NHẬP
 ══════════════════════════════ */
public User login(String username, String password) throws Exception {
    // 1. Tìm tài khoản trong Database theo username
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new Exception("Tên đăng nhập không tồn tại!"));

    // 2. Kiểm tra mật khẩu đã mã hóa BCrypt bằng passwordEncoder.matches
    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new Exception("Mật khẩu không chính xác!");
    }

    // 3. Kiểm tra xem tài khoản đã được Admin duyệt chưa
    if (user.getIsApproved() != null && !user.getIsApproved()) {
        throw new Exception("Tài khoản của bạn đang chờ quản trị viên phê duyệt!");
    }

    // 4. Kiểm tra xem tài khoản có bị khóa không
    if (user.getIsActive() != null && !user.getIsActive()) {
        throw new Exception("Tài khoản này đã bị khóa hoặc ngừng hoạt động!");
    }

    return user;
}
@Transactional
public User update(User user, Long roleId) {
    // 1. Tìm user gốc từ DB
    User existing = findById(user.getId());
    
    // 2. Cập nhật các thông tin cơ bản
    existing.setFullName(user.getFullName());
    existing.setEmail(user.getEmail());
    existing.setPhone(user.getPhone());
    existing.setIsActive(user.getIsActive());

    // 3. Xử lý đổi mật khẩu nếu có nhập mới
    if (user.getPassword() != null 
            && !user.getPassword().isBlank() 
            && !user.getPassword().startsWith("$2a$")) {
        existing.setPassword(passwordEncoder.encode(user.getPassword()));
    }
    
    // 4. LOGIC QUAN TRỌNG: Cập nhật lại vai trò mới vào bảng user_roles
    if (roleId != null) {
        Role newRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò có ID: " + roleId));
        
        existing.getRoles().clear(); // Xóa quyền cũ trong Set
        existing.getRoles().add(newRole); // Add quyền mới được chọn từ giao diện vào
    }

    return userRepository.save(existing);
}

@Transactional
public void updateProfile(User userDto) {
    User user = userRepository.findById(userDto.getId())
        .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
    
    user.setFullName(userDto.getFullName());
    user.setEmail(userDto.getEmail());
    // Có thể thêm cập nhật số điện thoại, địa chỉ...
    
    userRepository.save(user);
}
}