package com.backend.controller.swagger;

import com.backend.common.config.MemberPrincipal;
import com.backend.common.config.SwaggerError400;
import com.backend.common.config.SwaggerError401;
import com.backend.common.config.SwaggerError500;
import com.backend.domain.member.Member;
import com.backend.dto.BaseResponse;
import com.backend.dto.request.GoogleOauthLoginRequest;
import com.backend.dto.request.MemberSignupRequest;
import com.backend.dto.response.AuthTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "회원 API")
public interface MemberControllerSwagger {

    @Operation(
            summary = "구글 로그인",
            description = "구글 oauth 인가 코드를 바탕으로 로그인을 시도한다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공 시 토큰반환, 회원이 아닐 시 회원가입 토큰을 반환한다.",
                            content = @Content(schema = @Schema(implementation = AuthTokenResponse.class))
                    )
            }
    )
    @SwaggerError500
    ResponseEntity<BaseResponse<AuthTokenResponse>> oauthGoogleLogin(
            @Valid @RequestBody GoogleOauthLoginRequest request);

    @Operation(
            security = @SecurityRequirement(name = "SignupTokenAuth"),
            summary = "회원가입",
            description = "회원가입 토큰 헤더와 입력한 정보를 바탕으로 회원가입을 요청한다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "가입 성공 시 JWT 토큰 발급과 쿠키를 저장한다."
                    )
            }
    )
    @SwaggerError400
    @SwaggerError500
    ResponseEntity<BaseResponse<Void>> signup(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody MemberSignupRequest request);

    @Operation(
            summary = "로그아웃",
            description = "로그아웃한다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "쿠키를 삭제한다."
                    )
            }
    )
    @SwaggerError400
    @SwaggerError401
    @SwaggerError500
    ResponseEntity<BaseResponse<Void>> logout(
            @Parameter(hidden = true) @MemberPrincipal Member member,
            HttpServletRequest request);

    @Operation(
            summary = "액세스 토큰 재발급",
            description = "액세스 토큰 재발급을 요청한다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "액세스 토큰을 재발급과 쿠키를 저장한다."
                    )
            }
    )
    @SwaggerError400
    @SwaggerError401
    @SwaggerError500
    ResponseEntity<BaseResponse<Void>> reissueAccessToken(HttpServletRequest request);
}
