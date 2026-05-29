package thuvien.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublisherRequest {

    @NotBlank(message = "Tên nhà xuất bản không được để trống")
    @Size(max = 200)
    private String name;

    private String address;

    @Size(max = 20)
    private String phone;

    @Email(message = "Email không hợp lệ")
    @Size(max = 100)
    private String email;

    @Size(max = 200)
    private String website;
}