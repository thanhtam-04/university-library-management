package thuvien.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 6, message = "Mật khẩu mới phải ít nhất 6 ký tự")
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}