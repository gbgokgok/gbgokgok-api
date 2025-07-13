package com.backend.dto.request;

import com.backend.domain.member.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "회원가입 요청 DTO")
public record MemberSignupRequest(
        @Schema(description = "닉네임")
        @NotBlank(message = "닉네임이 존재하지 않습니다.") String nickname,
        @Schema(description = "생년월일", example = "2025-07-11")
        @NotNull(message = "생년월일이 존재하지 않습니다.") LocalDate birthDate,
        @Schema(description = "성별", example = "MALE")
        @NotNull(message = "성별이 존재하지 않습니다.") Gender gender) {

}
