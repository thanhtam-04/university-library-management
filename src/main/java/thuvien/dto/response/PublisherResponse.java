package thuvien.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublisherResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private LocalDateTime createdAt;
}