package thuvien.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse<LoanItemResponse> {
    private Long id;
    private String loanCode;
    private Long memberId;
    private String memberName;
    private String memberCardNumber;
    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private String status;
    private BigDecimal depositPaid;
    private BigDecimal depositRefunded;
    private List<LoanItemResponse> items;
    private String note;
}