package com.backend.dto.response;

import com.backend.domain.member.LoginType;
import com.backend.domain.member.Member;

public record GoogleOauthInfoApiResponse(String email, String picture) {

    public Member toMember() {
        return new Member(email, picture, LoginType.GOOGLE);
    }
}
