package com.backend.service;

import com.backend.common.exception.CustomException;
import com.backend.common.exception.ErrorCode;
import com.backend.domain.member.LoginType;
import com.backend.domain.member.Member;
import com.backend.domain.member.Role;
import com.backend.dto.AuthMember;
import com.backend.dto.request.GoogleOauthLoginRequest;
import com.backend.dto.request.MemberSignupRequest;
import com.backend.dto.response.AuthTokenResponse;
import com.backend.dto.response.GoogleOauthInfoApiResponse;
import com.backend.repository.MemberRepository;
import com.backend.service.jwt.JwtTokenProvider;
import com.backend.service.jwt.JwtTokenResolver;
import com.backend.service.oauth.OauthClient;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final OauthClient oauthClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenResolver jwtTokenResolver;
    private final MemberRepository memberRepository;

    // TODO: 카카오 로그인 추가 시 로그인 타입 수정
    @Transactional
    public AuthTokenResponse signup(String authorizationHeader, MemberSignupRequest request) {
        String token = jwtTokenResolver.extractBearerToken(authorizationHeader);
        Claims claims = jwtTokenResolver.resolveSignupToken(token);

        String email = claims.get("email", String.class);
        String picture = claims.get("picture", String.class);
//        LoginType loginType = claims.get("provider", LoginType.class);

        Member member = Member.create(
                email,
                request.nickname(),
                request.birthDate(),
                picture,
                request.gender(),
                Role.USER,
                LoginType.GOOGLE);

        if (memberRepository.existsByEmailAndLoginType(member.getEmail(), member.getLoginType())) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        memberRepository.save(member);

        return createAuthTokenResponse(member);
    }

    @Transactional
    public AuthTokenResponse googleLogin(GoogleOauthLoginRequest request) {
        GoogleOauthInfoApiResponse infoApiResponse = oauthClient.requestOauthInfo(request);

        Member member = infoApiResponse.toMember();

        return memberRepository.findByEmailAndLoginType(member.getEmail(), member.getLoginType())
                .map(this::createAuthTokenResponse)
                .orElseGet(() -> createSignupTokenResponse(infoApiResponse));
    }

    private AuthTokenResponse createAuthTokenResponse(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(member);
        String refreshToken = jwtTokenProvider.createRefreshToken(member);
        return AuthTokenResponse.of(accessToken, refreshToken);
    }

    private AuthTokenResponse createSignupTokenResponse(GoogleOauthInfoApiResponse oauthInfoApiResponse) {
        String signupToken = jwtTokenProvider.createSignupToken(oauthInfoApiResponse);
        return AuthTokenResponse.fromSignupToken(signupToken);
    }

    @Transactional(readOnly = true)
    public Member getAuthMember(String token) {
        AuthMember authMember = jwtTokenResolver.resolveAccessToken(token);
        return memberRepository.findById(authMember.id())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public void logout(String accessToken, String refreshToken) {
        AuthMember accessMember = jwtTokenResolver.resolveAccessToken(accessToken);
        AuthMember refreshMember = jwtTokenResolver.resolveRefreshToken(refreshToken);

        if (!accessMember.id().equals(refreshMember.id())) {
            throw new CustomException(ErrorCode.AUTHENTICATION_TOKEN_MEMBER_MISMATCH);
        }
    }
}
