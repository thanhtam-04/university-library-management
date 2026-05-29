package thuvien.dto.response;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanItemResponse {
    private Long id;
    private Long bookCopyId;
    private String barcode;
    private String bookTitle;
    private boolean returned;
    private LocalDateTime returnDate;
}