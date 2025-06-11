package com.backend.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {

    // TODO: 정규식 상수 추가

    @Column(nullable = false)
    private String email;

    public Email(String email) {
        validateEmailPattern(email);
        this.email = email;
    }

    private void validateEmailPattern(String email) {
        // TODO: 검증 추가
    }
}
