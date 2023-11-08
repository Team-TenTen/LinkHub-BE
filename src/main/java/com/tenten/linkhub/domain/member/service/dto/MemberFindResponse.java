package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Member;
import java.util.Optional;

public record MemberFindResponse(
        Long memberId
) {

    public static MemberFindResponse from(Optional<Member> member) {
        return member.map(entity -> new MemberFindResponse(entity.getId()))
                .orElseGet(() -> new MemberFindResponse(null));
    }

}
