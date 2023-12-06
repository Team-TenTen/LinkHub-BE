package com.tenten.linkhub.domain.space.facade.dto;

import com.tenten.linkhub.domain.space.model.space.Role;

public record SpaceInvitationFacadeRequest(
        String email,
        Long spaceId,
        Role role,
        Long myMemberId
) {
}
