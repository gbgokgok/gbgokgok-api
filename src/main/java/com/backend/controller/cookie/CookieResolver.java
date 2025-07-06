package com.backend.controller.cookie;

import com.backend.common.exception.CustomException;
import com.backend.common.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CookieResolver {

    public void checkLoginRequired(HttpServletRequest request) {
        if (isTokenEmpty(request)) {
            throw new CustomException(ErrorCode.TOKEN_EMPTY);
        }

        if (isRefreshTokenEmpty(request)) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EMPTY);
        }
    }

    public boolean isAccessTokenEmpty(HttpServletRequest request) {
        return isTokenEmpty(request, CookieProvider.ACCESS_TOKEN_COOKIE_NAME);
    }

    public boolean isRefreshTokenEmpty(HttpServletRequest request) {
        return isTokenEmpty(request, CookieProvider.REFRESH_TOKEN_COOKIE_NAME);
    }

    private boolean isTokenEmpty(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return true;
        }

        return Arrays.stream(request.getCookies())
                .noneMatch(cookie -> cookie.getName().equals(cookieName));
    }

    public String extractAccessToken(HttpServletRequest request) {
        return extractToken(request, CookieProvider.ACCESS_TOKEN_COOKIE_NAME)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_TOKEN_EMPTY));
    }

    public String extractRefreshToken(HttpServletRequest request) {
        return extractToken(request, CookieProvider.REFRESH_TOKEN_COOKIE_NAME)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_EMPTY));
    }

    private Optional<String> extractToken(HttpServletRequest request, String cookieName) {
        if (isTokenEmpty(request)) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .map(Cookie::getValue);
    }

    public boolean isTokenEmpty(HttpServletRequest request) {
        return isAccessTokenEmpty(request) && isRefreshTokenEmpty(request);
    }
}