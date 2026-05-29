package thuvien.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    private long totalBooks;
    private long totalCopies;
    private long availableCopies;
    private long borrowedCopies;
    private long totalMembers;
    private long activeMembers;
    private long totalLoans;
    private long overdueLoans;
    private BigDecimal totalDebt;
    private BigDecimal totalFineCollected;
}