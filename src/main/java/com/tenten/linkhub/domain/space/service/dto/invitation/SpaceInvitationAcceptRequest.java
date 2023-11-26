package com.tenten.linkhub.domain.space.service.dto.invitation;

public record SpaceInvitationAcceptRequest(
        Long memberId,
        Long notificationId
) {
}
