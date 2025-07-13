package com.backend.controller;

import com.backend.common.config.MemberPrincipal;
import com.backend.controller.cookie.CookieProvider;
import com.backend.controller.cookie.CookieResolver;
import com.backend.controller.swagger.MemberControllerSwagger;
import com.backend.domain.member.Member;
import com.backend.dto.BaseResponse;
import com.backend.dto.request.GoogleOauthLoginRequest;
import com.backend.dto.request.MemberSignupRequest;
import com.backend.dto.response.AuthTokenResponse;
import com.backend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController implements MemberControllerSwagger {

    private final CookieProvider cookieProvider;
    private final CookieResolver cookieResolver;
    private final MemberService memberService;

    @Override
    @PostMapping("/oauth/login")
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

    @Override
    @PostMapping("/oauth/signup")
    public ResponseEntity<BaseResponse<Void>> signup(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody MemberSignupRequest request) {
        AuthTokenResponse response = memberService.signup(authorizationHeader, request);

        ResponseCookie accessTokenCookie = cookieProvider.createAccessTokenCookie(response.accessToken());
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(response.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }

    @Override
    @GetMapping("/nicknames/{nickname}/available")
    public ResponseEntity<BaseResponse<Boolean>> checkNickname(
            @PathVariable("nickname") String nickname) {
        return ResponseEntity.ok(new BaseResponse<>(memberService.isAvailableNickname(nickname)));
    }

    @Override
    @PostMapping("/oauth/logout")
    public ResponseEntity<BaseResponse<Void>> logout(@MemberPrincipal Member member, HttpServletRequest request) {
        String accessToken = cookieResolver.extractAccessToken(request);
        String refreshToken = cookieResolver.extractRefreshToken(request);

        memberService.logout(accessToken, refreshToken);

        ResponseCookie deletedAccessTokenCookie = cookieProvider.deleteAccessTokenCookie();
        ResponseCookie deletedRefreshTokenCookie = cookieProvider.deleteRefreshTokenCookie();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, deletedAccessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, deletedRefreshTokenCookie.toString())
                .build();
    }

    @Override
    @PostMapping("/accessToken/reissue")
    public ResponseEntity<BaseResponse<Void>> reissueAccessToken(HttpServletRequest request) {
        cookieResolver.checkLoginRequired(request);

        String refreshToken = cookieResolver.extractRefreshToken(request);
        String accessToken = memberService.reissueAccessToken(refreshToken);

        ResponseCookie accessTokenCookie = cookieProvider.createAccessTokenCookie(accessToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .build();
    }
}
