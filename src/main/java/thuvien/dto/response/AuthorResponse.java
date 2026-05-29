package thuvien.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
    private Long id;
    private String fullName;
    private String bio;
    private String nationality;
    private LocalDateTime createdAt;
}