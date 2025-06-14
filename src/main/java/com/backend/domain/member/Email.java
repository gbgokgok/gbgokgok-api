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
public class Email {

    /**
     * 영문 대소문자, 숫자, 점, 하이픈, 언더스코어, 플러스 기호 포함을 허용한다.
     * @ 기호 뒤에 도메인 이름이 필요하며, 최소 2글자 이상의 최상위 도메인이 포함되어야 한다. e.g) .kr, .com 등
     */
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Column(nullable = false)
    private String email;

    public Email(String email) {
        validateEmailPattern(email);
        this.email = email;
    }

    private void validateEmailPattern(String email) {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new CustomException(ErrorCode.USER_EMAIL_INVALID_FORMAT);
        }
    }
}
