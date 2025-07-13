package com.backend.domain.member;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void 올바른_형식으로_이메일을_생성하면_예외가_발생하지_않는다() {
        assertThatCode(() -> new Email("no-reply@google.com"))
                .doesNotThrowAnyException();
    }

    @Test
    void 허용되지_않은_기호가_포함된_이메일이라면_예외가_발생한다() {
        assertThatThrownBy(() -> new Email("no^reply@google.com"));
    }

    @Test
    void local_part가_존재하지_않는_이메일이라면_예외가_발생한다() {
        assertThatThrownBy(() -> new Email("@google.com"));
    }

    @Test
    void 도메인이_존재하지_않는_이메일이라면_예외가_발생한다() {
        assertThatThrownBy(() -> new Email("no_reply@.com"));
    }

    @Test
    void 최상위_도메인이_존재하지_않는_이메일이라면_예외가_발생한다() {
        assertThatThrownBy(() -> new Email("no_reply@google"));
    }

    @Test
    void 최상위_도메인이_한글자인_이메일이라면_예외가_발생한다() {
        assertThatThrownBy(() -> new Email("no_reply@google.c"));
    }
}