package com.backend.domain.member;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void 올바른_형식으로_회원을_생성하면_예외가_발생하지_않는다() {
        assertThatCode(() -> Member.create(
                "test@gmail.com",
                "곡곡",
                LocalDate.of(1990, 3, 31),
                "https://profileImgUrl",
                Gender.MALE,
                Role.USER,
                LoginType.GOOGLE))
                .doesNotThrowAnyException();
    }
}