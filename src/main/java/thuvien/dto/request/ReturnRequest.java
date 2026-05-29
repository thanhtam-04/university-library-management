package thuvien.dto.request;

import lombok.*;

import java.util.List;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequest {

    @NotNull
    private Long loanId;

    private List<Long> bookCopyIds; // nếu trả một phần

    private String note;
}