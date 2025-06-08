package com.backend.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.ProblemDetail;

@Getter
@Schema(description = "API 공통 에러 응답 포맷")
public class CustomProblemDetail extends ProblemDetail {

    @Schema(description = "에러 이름")
    private final String errorName;

    public CustomProblemDetail(ProblemDetail problemDetail, String errorName) {
        super(problemDetail);
        this.errorName = errorName;
    }
}
