package thuvien.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private boolean active;
    private Set<String> roles;
    private LocalDateTime createdAt;
}