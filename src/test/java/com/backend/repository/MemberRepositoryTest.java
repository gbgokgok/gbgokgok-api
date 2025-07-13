package com.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.backend.domain.member.Email;
import com.backend.domain.member.Gender;
import com.backend.domain.member.LoginType;
import com.backend.domain.member.Member;
import com.backend.domain.member.Role;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberRepositoryTest extends BaseRepositoryTest{

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 이메일과_로그인_유형으로_회원을_조회한다() {
        // given
        save(Member.create("test@gmail.com",
                "곡곡",
                LocalDate.of(1990, 3, 31),
                "https://profileImgUrl",
                Gender.MALE,
                Role.USER,
                LoginType.GOOGLE));

        // when
        Optional<Member> member = memberRepository.findByEmailAndLoginType(new Email("test@gmail.com"), LoginType.GOOGLE);

        // then
        assertThat(member).isPresent();
        assertThat(member.get().getEmail().getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    void 존재하지_않는_회원을_조회하면_빈_Optional을_반환한다() {
        // given
        save(Member.create("test@gmail.com",
                "곡곡",
                LocalDate.of(1990, 3, 31),
                "https://profileImgUrl",
                Gender.MALE,
                Role.USER,
                LoginType.GOOGLE));

        // when
        Optional<Member> member = memberRepository.findByEmailAndLoginType(new Email("tes@gmail.com"), LoginType.GOOGLE);

        // then
        assertThat(member).isNotPresent();
    }

    @Test
    void 이메일과_로그인_유형으로_회원_중복을_검사한다() {
        // given
        save(Member.create("test@gmail.com",
                "곡곡",
                LocalDate.of(1990, 3, 31),
                "https://profileImgUrl",
                Gender.MALE,
                Role.USER,
                LoginType.GOOGLE));

        // when, then
        assertThat(memberRepository.existsByEmailAndLoginType(new Email("test@gmail.com"), LoginType.GOOGLE)).isTrue();
    }
}