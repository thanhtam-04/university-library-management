package thuvien.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String isbn;
    private String title;
    private String publisherName;
    private String categoryName;
    private Set<String> authors;
    private Integer totalCopies;
    private Integer availableCopies;
    private BigDecimal price;
    private BigDecimal depositFee;
    private String shelfLocation;
    private String coverImage;
    private String description;
    private String language;
    private Integer publicationYear;
    private LocalDateTime createdAt;
    private String summary;
	public void setEdition(String edition) {
		// TODO Auto-generated method stub
		
	}
	public BigDecimal getDepositAmount() {
		// TODO Auto-generated method stub
		return null;
	}
}