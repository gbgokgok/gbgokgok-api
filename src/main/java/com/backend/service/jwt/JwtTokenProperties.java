package com.backend.service.jwt;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProperties {

    protected static final String TOKEN_TYPE = "type";

    private final String secretKey;
    private final long accessTokenExpirationMillis;
    private final long refreshTokenExpirationMillis;
    private final long signupTokenExpirationMillis;

    public JwtTokenProperties(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.accessToken-expiration-millis}") long accessTokenExpirationMillis,
            @Value("${jwt.refreshToken-expiration-millis}") long refreshTokenExpirationMillis,
            @Value("${jwt.signupToken-expiration-millis}") long signupTokenExpirationMillis) {
        this.secretKey = secretKey;
        this.accessTokenExpirationMillis = accessTokenExpirationMillis;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
        this.signupTokenExpirationMillis = signupTokenExpirationMillis;
    }

    public long getAccessTokenExpirationMillis() {
        return accessTokenExpirationMillis;
    }

    public long getRefreshTokenExpirationMillis() {
        return refreshTokenExpirationMillis;
    }

    public long getSignupTokenExpirationMillis() {
        return signupTokenExpirationMillis;
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
