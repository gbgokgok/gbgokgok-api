package com.backend.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@Schema(description = "API 공통 에러 응답 포맷")
public class CustomProblemDetail extends ProblemDetail {

    @Schema(description = "에러 이름")
    private final String errorName;

    @Schema(description = "에러 메시지")
    private List<String> details;

    public CustomProblemDetail(ProblemDetail problemDetail, String errorName) {
        super(problemDetail);
        this.errorName = errorName;
    }

    public CustomProblemDetail(ProblemDetail problemDetail, String errorName, MethodArgumentNotValidException e) {
        super(problemDetail);
        this.errorName = errorName;
        this.details = getErrors(e);
    }

    private List<String> getErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(error -> "%s : %s".formatted(error.getField(), error.getDefaultMessage()))
                .toList();
    }
}
