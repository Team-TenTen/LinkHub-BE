package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.model.Role;

public record MemberAuthInfo(Long memberId, String socialId, Provider provider, Role role) {

    public static MemberAuthInfo from(Member updatedMember) {
        return new MemberAuthInfo(
                updatedMember.getId(),
                updatedMember.getSocialId(),
                updatedMember.getProvider(),
                updatedMember.getRole()
        );
    }
}