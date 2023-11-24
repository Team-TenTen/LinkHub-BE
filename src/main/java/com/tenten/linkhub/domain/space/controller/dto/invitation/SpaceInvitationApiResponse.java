package com.tenten.linkhub.domain.space.controller.dto.invitation;

public record SpaceInvitationApiResponse(Long notificationId) {
    public static SpaceInvitationApiResponse from(Long savedNotificationId) {
        return new SpaceInvitationApiResponse(savedNotificationId);
    }
}
