package com.tenten.linkhub.domain.member.service.dto;

public record MemberFollowersFindResponse(
        Long memberId,
        String nickname,
        String aboutMe,
        String profileImagePath,
        Boolean isFollowing
) {
}