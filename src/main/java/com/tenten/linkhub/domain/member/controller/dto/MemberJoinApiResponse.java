package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberJoinResponse;

public record MemberJoinApiResponse(String jwt) {

    public static MemberJoinApiResponse from(MemberJoinResponse memberJoinResponse) {
        return new MemberJoinApiResponse(memberJoinResponse.jwt());
    }
}
