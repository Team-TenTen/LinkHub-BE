package com.tenten.linkhub.domain.member.controller.dto;

public record MemberSearchApiResponse(
        Long memberId,
        String aboutMe,
        String nickname,
        String MemberImagePath,
        Boolean isFollowing
) {

}
