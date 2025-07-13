package com.backend.repository;

import com.backend.domain.member.Email;
import com.backend.domain.member.LoginType;
import com.backend.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndLoginType(Email email, LoginType loginType);

    boolean existsByEmailAndLoginType(Email email, LoginType loginType);
}
