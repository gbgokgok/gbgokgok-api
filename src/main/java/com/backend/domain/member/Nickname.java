package com.backend.domain.member;

import com.backend.common.exception.CustomException;
import com.backend.common.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname {

    private static final Pattern KOREAN_NICKNAME_REGEX = Pattern.compile("^[가-힣0-9]{2,6}$");
    private static final Pattern ENGLISH_NICKNAME_REGEX = Pattern.compile("^[a-zA-Z0-9]{2,14}$");
    private static final Pattern NUMERIC_ONLY_NICKNAME_REGEX = Pattern.compile("^[0-9]+$");

    @Column(nullable = false)
    private String nickname;

    public Nickname(String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }

    private void validateNickname(String nickname) {
        if (NUMERIC_ONLY_NICKNAME_REGEX.matcher(nickname).matches()) {
            throw new CustomException(ErrorCode.USER_NICKNAME_INVALID_FORMAT);
        }

        if (!KOREAN_NICKNAME_REGEX.matcher(nickname).matches() && !ENGLISH_NICKNAME_REGEX.matcher(nickname).matches()) {
            throw new CustomException(ErrorCode.USER_NICKNAME_INVALID_FORMAT);
        }
    }
}
