package com.backend.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname {

    // TODO: 길이, 정규식 상수 추가

    @Column(nullable = false)
    private String nickname;

    public Nickname(String nickname) {
        validateLength(nickname);
        validateRegex(nickname);
        this.nickname = nickname;
    }

    private void validateLength(String nickname) {

    }

    private void validateRegex(String nickname) {

    }
}
