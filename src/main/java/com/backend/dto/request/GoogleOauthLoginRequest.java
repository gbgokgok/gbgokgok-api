package com.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GoogleOauthLoginRequest(@NotBlank(message = "인가 코드가 존재하지 않습니다.") String code,
                                      @NotBlank(message = "Redirect URI가 존재하지 않습니다.") String redirectUri) {

}
