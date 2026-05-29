package thuvien.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;

    private String studentCode;
    private String department;
    private String course;

    @FutureOrPresent(message = "Ngày hết hạn phải từ hôm nay trở đi")
    private LocalDate cardExpiryDate;

    private Integer maxBorrowLimit = 5;
}