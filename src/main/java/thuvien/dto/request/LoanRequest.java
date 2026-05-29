package thuvien.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @NotNull(message = "Mã bạn đọc không được để trống")
    private Long memberId;

    @NotEmpty(message = "Phải chọn ít nhất một cuốn sách")
    private List<Long> bookCopyIds;
}