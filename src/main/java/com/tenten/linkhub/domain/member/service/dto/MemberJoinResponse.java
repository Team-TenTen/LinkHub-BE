package com.tenten.linkhub.domain.member.service.dto;

public record MemberJoinResponse(String jwt) {

    public static MemberJoinResponse from(String jwt) {
        return new MemberJoinResponse(jwt);
    }
}
