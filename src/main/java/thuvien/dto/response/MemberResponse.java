package thuvien.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String cardNumber;
    private String fullName;
    private String studentCode;
    private String department;
    private String course;
    private String email;
    private String phone;
    private String status;
    private LocalDate cardIssuedDate;
    private LocalDate cardExpiryDate;
    private Integer maxBorrowLimit;
    private Integer totalBorrowed;
    private BigDecimal currentDebt;
}