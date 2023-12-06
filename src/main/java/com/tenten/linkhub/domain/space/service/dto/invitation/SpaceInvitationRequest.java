package com.tenten.linkhub.domain.space.service.dto.invitation;

import com.tenten.linkhub.domain.space.model.space.Role;

public record SpaceInvitationRequest(
        Long spaceId,
        Role role,
        Long notificationId,
        Long memberId
) {
}
