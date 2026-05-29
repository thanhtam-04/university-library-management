package thuvien.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 500)
    private String avatar;

    /** true = tài khoản đang hoạt động, false = bị khoá */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * true  = admin đã duyệt → có thể đăng nhập
     * false = đang chờ duyệt (mặc định sau khi đăng ký)
     */
    @Column(name = "is_approved")
    private Boolean isApproved = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns        = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // ── Alias helpers (tương thích với UserService.setActive / isActive) ──

    /** Alias setIsActive — dùng khi gọi user.setActive(true) */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /** Alias getIsActive — dùng khi gọi user.isActive() */
    public boolean isActive() {
        return Boolean.TRUE.equals(this.isActive);
    }

    /** Alias getIsApproved — dùng khi gọi user.isApproved() */
    public boolean isApproved() {
        return Boolean.TRUE.equals(this.isApproved);
    }

	public void save(User user) {
		// TODO Auto-generated method stub
		
	}
	public void setIsActive(boolean isActive) {
	    this.isActive = isActive;
	}

	public Object getMember() {
		// TODO Auto-generated method stub
		return null;
	}
}