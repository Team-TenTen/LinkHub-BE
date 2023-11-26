package com.tenten.linkhub.domain.space.controller.dto.invitation;

public record SpaceInvitationAcceptApiResponse(
        Long spaceId
) {
    public static SpaceInvitationAcceptApiResponse from(Long spaceId) {
        return new SpaceInvitationAcceptApiResponse(spaceId);
    }
}
