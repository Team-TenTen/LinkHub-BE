package com.tenten.linkhub.domain.member.service.dto;

public record MemberSearchResponse(
        Long memberId,
        String aboutMe,
        String nickname,
        String memberImagePath,
        Boolean isFollowing
) {

}
