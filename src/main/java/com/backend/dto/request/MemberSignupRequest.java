package com.backend.dto.request;

import com.backend.domain.member.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MemberSignupRequest(@NotBlank(message = "닉네임이 존재하지 않습니다.") String nickname,
                                  @NotNull(message = "생년월일이 존재하지 않습니다.") LocalDate birthDate,
                                  @NotNull(message = "성별이 존재하지 않습니다.") Gender gender) {

}
