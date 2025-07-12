package com.backend.service.jwt;

import com.backend.common.exception.CustomException;
import com.backend.common.exception.ErrorCode;
import com.backend.dto.AuthMember;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenResolver {

    private final JwtTokenProperties jwtTokenProperties;

    public AuthMember resolveAccessToken(String token) {
        return (AuthMember) resolveTokenByType(token, TokenType.ACCESS_TOKEN);
    }

    public AuthMember resolveRefreshToken(String token) {
        return (AuthMember) resolveTokenByType(token, TokenType.REFRESH_TOKEN);
    }

    public Claims resolveSignupToken(String token) {
        return (Claims) resolveTokenByType(token, TokenType.SIGNUP_TOKEN);
    }

    private Object resolveTokenByType(String token, TokenType tokenType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtTokenProperties.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            validateTokenType(claims, tokenType);

            if (tokenType.equals(TokenType.SIGNUP_TOKEN)) {
                return claims;
            }

            Long id = Long.valueOf(claims.getSubject());
            return AuthMember.from(id);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.AUTHENTICATION_TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
        }
    }

    public String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.AUTHORIZATION_HEADER_MISSING);
        }

        return authorizationHeader.substring(7);
    }

    private void validateTokenType(Claims claims, TokenType tokenType) {
        String extractTokenType = claims.get(JwtTokenProperties.TOKEN_TYPE, String.class);
        if (!extractTokenType.equals(tokenType.name())) {
            throw new CustomException(ErrorCode.AUTHENTICATION_TOKEN_TYPE_MISMATCH);
        }
    }
}
