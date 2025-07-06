package com.backend.service;

import com.backend.domain.member.Member;
import com.backend.dto.request.GoogleOauthLoginRequest;
import com.backend.dto.response.AuthTokenResponse;
import com.backend.dto.response.GoogleOauthInfoApiResponse;
import com.backend.repository.MemberRepository;
import com.backend.service.jwt.JwtTokenProvider;
import com.backend.service.oauth.OauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final OauthClient oauthClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

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
}
