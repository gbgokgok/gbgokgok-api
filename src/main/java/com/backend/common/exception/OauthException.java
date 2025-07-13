package com.backend.common.exception;

import lombok.Getter;

@Getter
public class OauthException extends RuntimeException {

    private final OauthExceptionResponse response;

    public OauthException(OauthExceptionResponse response) {
        super(response.error() + " - " + response.error_description());
        this.response = response;
    }
}
