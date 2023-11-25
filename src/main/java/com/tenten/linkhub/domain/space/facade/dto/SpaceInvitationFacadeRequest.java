package com.tenten.linkhub.domain.space.facade.dto;

import com.tenten.linkhub.domain.space.model.space.Role;

public record SpaceInvitationFacadeRequest(
        Long memberId,
        Long spaceId,
        Role role,
        Long myMemberId
) {
}
