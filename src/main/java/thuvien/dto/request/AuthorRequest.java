package thuvien.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRequest {

    @NotBlank(message = "Tên tác giả không được để trống")
    @Size(max = 200, message = "Tên tác giả tối đa 200 ký tự")
    private String fullName;

    @Size(max = 2000, message = "Tiểu sử tối đa 2000 ký tự")
    private String bio;

    @Size(max = 100, message = "Quốc tịch tối đa 100 ký tự")
    private String nationality;
}