package com.tenten.linkhub.domain.member.controller.dto;

public record MemberFollowersFindApiResponse(
        Long memberId,
        String nickname,
        String aboutMe,
        String profileImagePath,
        Boolean isFollowing
) {
}
