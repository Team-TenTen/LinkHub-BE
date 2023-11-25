package com.tenten.linkhub.domain.member.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.member.model.Member;

public record MemberWithProfileImageAndFollowingStatus(
        Member member,
        Boolean isFollowing
) {

    @QueryProjection
    public MemberWithProfileImageAndFollowingStatus(Member member, Boolean isFollowing) {
        this.member = member;
        this.isFollowing = isFollowing;
    }

}
