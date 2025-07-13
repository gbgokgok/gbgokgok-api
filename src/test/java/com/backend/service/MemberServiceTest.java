package com.backend.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.backend.common.exception.CustomException;
import com.backend.common.exception.ErrorCode;
import com.backend.domain.member.Gender;
import com.backend.domain.member.LoginType;
import com.backend.domain.member.Member;
import com.backend.domain.member.Role;
import com.backend.dto.request.GoogleOauthLoginRequest;
import com.backend.dto.request.MemberSignupRequest;
import com.backend.dto.response.AuthTokenResponse;
import com.backend.dto.response.GoogleOauthInfoApiResponse;
import com.backend.repository.MemberRepository;
import com.backend.service.jwt.JwtTokenProperties;
import com.backend.service.jwt.JwtTokenProvider;
import com.backend.service.jwt.JwtTokenResolver;
import com.backend.service.jwt.TokenType;
import com.backend.service.oauth.OauthClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class MemberServiceTest extends BaseServiceTest {

    @MockBean
    private OauthClient oauthClient;

    @MockBean
    private JwtTokenResolver jwtTokenResolver;

    @Autowired
    private MemberService memberService;

    @Test
    void 회원가입_시_JWT_토큰을_반환한다() {
        // given
        String authorizationHeader = "Bearer SIGNUP_TOKEN";
        MemberSignupRequest request = new MemberSignupRequest("곡곡", LocalDate.of(2025, 7, 11), Gender.MALE);

        Claims claims = Jwts.claims();
        claims.put("email", "test@gmail.com");
        claims.put("picture", "https://img");
        claims.put("token", TokenType.SIGNUP_TOKEN.name());

        String extractBearerToken = "SIGNUP_TOKEN";
        when(jwtTokenResolver.extractBearerToken(authorizationHeader)).thenReturn(extractBearerToken);
        when(jwtTokenResolver.resolveSignupToken(extractBearerToken)).thenReturn(claims);

        // when
        AuthTokenResponse response = memberService.signup(authorizationHeader, request);

        // then
        assertAll(
                () -> assertThat(response.accessToken()).isNotBlank(),
                () -> assertThat(response.refreshToken()).isNotBlank()
        );
    }

    @Test
    void 회원가입_시_이미_가입된_회원이라면_예외가_발생한다() {
        // given
        Member member = Member.create("test@gmail.com",
                "곡곡",
                LocalDate.of(1990, 3, 31),
                "https://profileImgUrl",
                Gender.MALE,
                Role.USER,
                LoginType.GOOGLE);
        memberRepository.save(member);

        String authorizationHeader = "Bearer SIGNUP_TOKEN";
        MemberSignupRequest request = new MemberSignupRequest("공공", LocalDate.of(2025, 7, 11), Gender.MALE);

        Claims claims = Jwts.claims();
        claims.put("email", "test@gmail.com");
        claims.put("picture", "https://img");
        claims.put("token", TokenType.SIGNUP_TOKEN.name());

        String extractBearerToken = "SIGNUP_TOKEN";
        when(jwtTokenResolver.extractBearerToken(authorizationHeader)).thenReturn(extractBearerToken);
        when(jwtTokenResolver.resolveSignupToken(extractBearerToken)).thenReturn(claims);

        // when, then
        assertThatThrownBy(() -> memberService.signup(authorizationHeader, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_ALREADY_EXIST.getMessage());
    }

    @Test
    void 회원가입_시_중복된_닉네임이라면_예외가_발생한다() {
        // given
        Member member = Member.create("test@gmail.com",
                "곡곡",
                LocalDate.of(1990, 3, 31),
                "https://profileImgUrl",
                Gender.MALE,
                Role.USER,
                LoginType.GOOGLE);
        memberRepository.save(member);

        String authorizationHeader = "Bearer SIGNUP_TOKEN";
        MemberSignupRequest request = new MemberSignupRequest("곡곡", LocalDate.of(2025, 7, 11), Gender.MALE);

        Claims claims = Jwts.claims();
        claims.put("email", "test@gmail.com");
        claims.put("picture", "https://img");
        claims.put("token", TokenType.SIGNUP_TOKEN.name());

        String extractBearerToken = "SIGNUP_TOKEN";
        when(jwtTokenResolver.extractBearerToken(authorizationHeader)).thenReturn(extractBearerToken);
        when(jwtTokenResolver.resolveSignupToken(extractBearerToken)).thenReturn(claims);

        // when, then
        assertThatThrownBy(() -> memberService.signup(authorizationHeader, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_NICKNAME_ALREADY_EXIST.getMessage());
    }

    @Test
    void 사용_가능한_닉네임일_경우_true를_반환한다() {
        // given, when, then
        assertThat(memberService.isAvailableNickname("곡곡")).isTrue();
    }

    @Test
    void 중복된_닉네임일_경우_false를_반환한다() {
        // given
        Member member = Member.create("test@gmail.com",
                "곡곡",
                LocalDate.of(1990, 3, 31),
                "https://profileImgUrl",
                Gender.MALE,
                Role.USER,
                LoginType.GOOGLE);
        memberRepository.save(member);

        // when, then
        assertThat(memberService.isAvailableNickname("곡곡")).isFalse();
    }

    @Test
    void 기존_회원이_구글_로그인에_성공할_시_JWT_토큰을_반환한다() {
        // given
        Member member = Member.create("test@gmail.com",
                "곡곡",
                LocalDate.of(1990, 3, 31),
                "https://profileImgUrl",
                Gender.MALE,
                Role.USER,
                LoginType.GOOGLE);
        memberRepository.save(member);

        GoogleOauthLoginRequest request = new GoogleOauthLoginRequest("authCode", "redirectUrl");
        GoogleOauthInfoApiResponse infoApiResponse = new GoogleOauthInfoApiResponse("test@gmail.com", "https://picture");

        when(oauthClient.requestOauthInfo(request)).thenReturn(infoApiResponse);

        // when
        AuthTokenResponse response = memberService.googleLogin(request);

        // then
        assertAll(
                () -> assertThat(response.accessToken()).isNotBlank(),
                () -> assertThat(response.refreshToken()).isNotBlank(),
                () -> assertThat(response.isSignupRequired()).isFalse()
        );
    }

    @Test
    void 신규_회원이_구글_로그인을_시도했을_시_회원가입_토큰을_반환한다() {
        // given
        GoogleOauthLoginRequest request = new GoogleOauthLoginRequest("authCode", "redirectUrl");
        GoogleOauthInfoApiResponse infoApiResponse = new GoogleOauthInfoApiResponse("test@gmail.com", "https://picture");

        when(oauthClient.requestOauthInfo(request)).thenReturn(infoApiResponse);

        // when
        AuthTokenResponse response = memberService.googleLogin(request);

        // then
        assertAll(
                () -> assertThat(response.accessToken()).isNotBlank(),
                () -> assertThat(response.refreshToken()).isNull(),
                () -> assertThat(response.isSignupRequired()).isTrue()
        );
    }
}