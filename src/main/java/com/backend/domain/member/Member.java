package com.backend.domain.member;

import com.backend.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Nickname nickname;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String profileImgUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private Member(String email, String nickname, LocalDate birthDate, String profileImgUrl, Gender gender, Role role, LoginType loginType) {
        this.email = new Email(email);
        this.nickname = new Nickname(nickname);
        this.birthDate = birthDate;
        this.profileImgUrl = profileImgUrl;
        this.gender = gender;
        this.role = role;
        this.loginType = loginType;
    }

    public static Member create(String email, String nickname, LocalDate birthDate, String profileImgUrl, Gender gender, Role role, LoginType loginType) {
        return new Member(
                email,
                nickname,
                birthDate,
                profileImgUrl,
                gender,
                role,
                loginType
        );
    }
}
