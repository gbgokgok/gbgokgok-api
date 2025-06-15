package com.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleOauthTokenResponse(String access_token, String expires_in, String token_type,
                                       String scope, String refresh_token) {

}
