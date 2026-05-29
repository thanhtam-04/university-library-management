package thuvien.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FineResponse {
    private Long id;
    private Long loanId;
    private String memberName;
    private BigDecimal fineAmount;
    private int daysOverdue;
    private BigDecimal finePerDay;
    private String reason;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}