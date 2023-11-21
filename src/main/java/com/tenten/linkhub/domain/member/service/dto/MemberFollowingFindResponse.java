package com.tenten.linkhub.domain.member.service.dto;

public record MemberFollowingFindResponse(
        Long memberId,
        String nickname,
        String profileImagePath,
        Boolean isFollowing
) {

}