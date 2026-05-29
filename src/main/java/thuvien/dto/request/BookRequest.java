package thuvien.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "ISBN không được để trống")
    @Size(max = 20, message = "ISBN tối đa 20 ký tự")
    private String isbn;

    @NotBlank(message = "Tên sách không được để trống")
    @Size(max = 500, message = "Tên sách tối đa 500 ký tự")
    private String title;

    private Long publisherId;

    private Long categoryId;

    @Min(value = 1000, message = "Năm xuất bản không hợp lệ")
    @Max(value = 2100, message = "Năm xuất bản không hợp lệ")
    private Integer publicationYear;

    private String edition;
    private String language = "Tiếng Việt";

    @Min(value = 0, message = "Số lượng sách phải >= 0")
    private Integer totalCopies = 0;

    @PositiveOrZero(message = "Giá sách phải >= 0")
    private BigDecimal price;

    @PositiveOrZero(message = "Phí cọc phải >= 0")
    private BigDecimal depositFee = BigDecimal.ZERO;

    private String shelfLocation;
    private String description;
    private String coverImage;
}