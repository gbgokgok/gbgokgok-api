package com.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 토큰 응답 DTO")
public record AuthTokenResponse(
        @Schema(description = "액세스 토큰") String accessToken,
        @Schema(description = "리프레시 토큰") String refreshToken,
        @Schema(description = "회원가입 필요 여부") boolean isSignupRequired) {

    public static AuthTokenResponse of(String accessToken, String refreshToken) {
        return new AuthTokenResponse(accessToken, refreshToken, false);
    }

    public static AuthTokenResponse fromSignupToken(String signupToken) {
        return new AuthTokenResponse(signupToken, null, true);
    }
}
