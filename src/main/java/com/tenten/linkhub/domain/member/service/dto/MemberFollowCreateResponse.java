package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Member;

public record MemberFollowCreateResponse(Long followerId) {
    public static MemberFollowCreateResponse from(Member follower) {
        return new MemberFollowCreateResponse(follower.getId());
    }
}
