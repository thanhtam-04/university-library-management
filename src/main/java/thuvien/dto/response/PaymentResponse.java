package thuvien.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long memberId;
    private String memberName;
    private BigDecimal amount;
    private String paymentType;     // FINE, DEPOSIT, DEPOSIT_REFUND
    private String paymentMethod;   // CASH, TRANSFER
    private Long fineId;
    private String note;
    private LocalDateTime createdAt;
}