package com.tenten.linkhub.domain.member.service.dto;

public record MemberUpdateResponse(Long memberId) {

    public static MemberUpdateResponse from(Long memberId) {
        return new MemberUpdateResponse(memberId);
    }
}
