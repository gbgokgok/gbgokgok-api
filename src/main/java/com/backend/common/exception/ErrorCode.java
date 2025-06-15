package com.backend.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // Auth
    OAUTH_TOKEN_INTERNAL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 서버와 통신 중 예상치 못한 예외가 발생했습니다."),
    OAUTH_REDIREC_URI_MISMATCH(HttpStatus.BAD_REQUEST, "일치하는 Redirect URI가 존재하지 않습니다."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // User
    USER_EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),
    USER_NICKNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "닉네임은 한글(숫자 포함) 2~6자 또는 영어(숫자 포함) 2~14자만 가능합니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
