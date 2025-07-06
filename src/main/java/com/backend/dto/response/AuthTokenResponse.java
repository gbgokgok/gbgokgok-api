package com.backend.dto.response;

public record AuthTokenResponse(String accessToken, String refreshToken, boolean isSignupRequired) {

    public static AuthTokenResponse of(String accessToken, String refreshToken) {
        return new AuthTokenResponse(accessToken, refreshToken, false);
    }

    public static AuthTokenResponse fromSignupToken(String signupToken) {
        return new AuthTokenResponse(signupToken, null, true);
    }
}
