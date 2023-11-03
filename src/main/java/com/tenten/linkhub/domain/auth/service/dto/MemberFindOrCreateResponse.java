package com.tenten.linkhub.domain.auth.service.dto;

import com.tenten.linkhub.domain.member.model.Member;

public record MemberFindOrCreateResponse(
        Long memberId,
        Boolean isLoggedIn
) {

    public static MemberFindOrCreateResponse from(Member member) {
        boolean isLoggedIn = true;

        String nickname = member.getNickname();

        if (nickname == null) {
            isLoggedIn = false;
        }

        return new MemberFindOrCreateResponse(
                member.getId(),
                isLoggedIn
        );
    }
}
