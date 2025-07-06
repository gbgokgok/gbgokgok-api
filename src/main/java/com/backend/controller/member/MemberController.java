package com.backend.controller.member;

import com.backend.controller.cookie.CookieProvider;
import com.backend.dto.BaseResponse;
import com.backend.dto.request.GoogleOauthLoginRequest;
import com.backend.dto.response.AuthTokenResponse;
import com.backend.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final CookieProvider cookieProvider;
    private final MemberService memberService;

    @RequestMapping("/oauth/login")
    public ResponseEntity<BaseResponse<AuthTokenResponse>> oauthGoogleLogin(@Valid @RequestBody GoogleOauthLoginRequest request) {
        AuthTokenResponse response = memberService.googleLogin(request);

        if (response.isSignupRequired()) {
            return ResponseEntity.ok(new BaseResponse<>(response));
        }

        ResponseCookie accessTokenCookie = cookieProvider.createAccessTokenCookie(response.accessToken());
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(response.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }
}
