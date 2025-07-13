package com.backend.domain.member;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.backend.common.exception.CustomException;
import org.junit.jupiter.api.Test;

class NicknameTest {

    @Test
    void 올바른_형식으로_닉네임을_생성하면_예외가_발생하지_않는다() {
        assertThatCode(() -> new Nickname("곡곡2"))
                .doesNotThrowAnyException();
    }

    @Test
    void 닉네임이_2글자_이하면_예외가_발생한다() {
        assertThatThrownBy(() -> new Nickname("곡"))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 닉네임이_숫자만_입력되면_예외가_발생한다() {
        assertThatThrownBy(() -> new Nickname("123"))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 한글_닉네임이_6글자를_초과하면_예외가_발생한다() {
        assertThatThrownBy(() -> new Nickname("닉네임글자초과"))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 영어_닉네임이_14글자를_초과하면_예외가_발생한다() {
        assertThatThrownBy(() -> new Nickname("Exceeding14Characters"))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 한글과_영어가_혼용된_닉네임은_예외가_발생한다() {
        assertThatThrownBy(() -> new Nickname("곡곡GokGok"))
                .isInstanceOf(CustomException.class);
    }
}