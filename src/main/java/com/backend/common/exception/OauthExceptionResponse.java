package com.backend.common.exception;

public record OauthExceptionResponse(String error, String error_description, String error_code) {

}
