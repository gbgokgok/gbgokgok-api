package com.backend.service.jwt;

import com.backend.domain.member.LoginType;
import com.backend.domain.member.Member;
import com.backend.dto.response.GoogleOauthInfoApiResponse;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private static final String SIGNUP_SUBJECT = "signup";

    private final JwtTokenProperties jwtTokenProperties;

    public String createAccessToken(Member member) {
        long expirationMillis = jwtTokenProperties.getAccessTokenExpirationMillis();
        return createToken(member, expirationMillis, TokenType.ACCESS_TOKEN);
    }

    public String createRefreshToken(Member member) {
        long expirationMillis = jwtTokenProperties.getRefreshTokenExpirationMillis();
        return createToken(member, expirationMillis, TokenType.REFRESH_TOKEN);
    }

    // TODO: 카카오 로그인 추가 시 메소드 수정
    public String createSignupToken(GoogleOauthInfoApiResponse oauthInfoApiResponse) {
        long expirationMillis = jwtTokenProperties.getSignupTokenExpirationMillis();

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMillis);

        Map<String, Object> claims = Map.of(
                "email", oauthInfoApiResponse.email(),
                "picture", oauthInfoApiResponse.picture(),
                "provider", LoginType.GOOGLE
        );

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(SIGNUP_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .claim(JwtTokenProperties.TOKEN_TYPE, TokenType.SIGNUP_TOKEN)
                .signWith(jwtTokenProperties.getSecretKey());
        claims.forEach(jwtBuilder::claim);

        return jwtBuilder.compact();
    }

    private String createToken(Member member, long expirationMillis, TokenType tokenType) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .claim(jwtTokenProperties.TOKEN_TYPE, tokenType.name())
                .signWith(jwtTokenProperties.getSecretKey())
                .compact();
    }
}
