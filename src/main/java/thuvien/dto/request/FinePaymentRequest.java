package thuvien.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinePaymentRequest {

    @NotNull
    private Long fineId;

    @Positive(message = "Số tiền phải lớn hơn 0")
    private BigDecimal amount;

    private String paymentMethod = "CASH"; // CASH hoặc TRANSFER
}