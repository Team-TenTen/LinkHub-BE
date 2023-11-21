package com.tenten.linkhub.domain.member.controller.dto;

public record MemberFollowingsFindApiResponse(
        Long memberId,
        String nickname,
        String profileImagePath,
        Boolean isFollowing
) {
}
