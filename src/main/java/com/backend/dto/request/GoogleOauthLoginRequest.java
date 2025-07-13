package com.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "구글 oauth 로그인 요청 DTO")
public record GoogleOauthLoginRequest(
        @Schema(description = "구글 인가 코드")
        @NotBlank(message = "인가 코드가 존재하지 않습니다.") String code,
        @Schema(description = "리디렉션 URI")
        @NotBlank(message = "Redirect URI가 존재하지 않습니다.") String redirectUri) {

}
