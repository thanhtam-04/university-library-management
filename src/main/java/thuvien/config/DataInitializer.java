package thuvien.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import thuvien.entity.Role;
import thuvien.entity.Role.RoleName;
import thuvien.entity.User;
import thuvien.repository.RoleRepository;
import thuvien.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Tạo các vai trò nếu chưa có
        Role adminRole = createRoleIfNotFound(RoleName.ROLE_ADMIN);
        createRoleIfNotFound(RoleName.ROLE_LIBRARIAN);
        createRoleIfNotFound(RoleName.ROLE_STUDENT);

        // 2. Kiểm tra tài khoản admin cũ
        String adminUsername = "admin";
        
        if (userRepository.existsByUsername(adminUsername)) {
            // NẾU ĐÃ CÓ ADMIN: Lấy ra và ép lại mật khẩu chuẩn BCrypt phòng trường hợp mật khẩu cũ lỗi
            User existingAdmin = userRepository.findByUsername(adminUsername).orElse(null);
            if (existingAdmin != null) {
                // Ép mật khẩu mã hóa BCrypt chuẩn chỉ cho admin123
                existingAdmin.setPassword(passwordEncoder.encode("admin123")); 
                existingAdmin.setIsActive(true);
                existingAdmin.setIsApproved(true); // Đảm bảo trạng thái đã phê duyệt
                userRepository.save(existingAdmin);
                System.out.println("=== [HỆ THỐNG] Đã cập nhật mật khẩu BCrypt thành công cho Admin cũ! ===");
            }
        } else {
            // NẾU CHƯA CÓ ADMIN: Tiến hành tạo mới hoàn toàn
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode("admin123")); // Mã hóa chuẩn BCrypt
            admin.setFullName("Quản Trị Viên");
            admin.setEmail("admin@libmanage.com");
            admin.setPhone("0999999999");
            admin.setIsActive(true);
            admin.setIsApproved(true); // Được duyệt thẳng
            admin.setCreatedAt(LocalDateTime.now());
            admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
            
            userRepository.save(admin);
            System.out.println("=== [HỆ THỐNG] Đã khởi tạo mới thành công Admin chuẩn BCrypt! ===");
        }
    }

    private Role createRoleIfNotFound(RoleName roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            return roleRepository.save(role);
        });
    }
}