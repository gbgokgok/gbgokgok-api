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
    OAUTH_REDIRECT_URI_MISMATCH(HttpStatus.BAD_REQUEST, "일치하는 Redirect URI가 존재하지 않습니다."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    ACCESS_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "액세스 토큰이 존재하지 않습니다. 액세스 토큰을 발급해주세요."),
    REFRESH_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다. 다시 로그인해주세요."),
    TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "로그인이 필요한 사용자입니다."),
    AUTHENTICATION_TOKEN_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "올바르지 않은 토큰 타입입니다."),
    AUTHENTICATION_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    AUTHENTICATION_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰 정보입니다."),
    AUTHORIZATION_HEADER_MISSING(HttpStatus.BAD_REQUEST, "Authorization 헤더가 존재하지 않습니다."),
    AUTHENTICATION_TOKEN_MEMBER_MISMATCH(HttpStatus.UNAUTHORIZED, "엑세스 토큰과 리프레시 토큰의 소유자가 다릅니다."),

    // Member
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 회원가입 된 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 회원입니다."),
    MEMBER_EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),
    MEMBER_NICKNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "닉네임은 한글(숫자 포함) 2~6자 또는 영어(숫자 포함) 2~14자만 가능합니다."),
    MEMBER_NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
