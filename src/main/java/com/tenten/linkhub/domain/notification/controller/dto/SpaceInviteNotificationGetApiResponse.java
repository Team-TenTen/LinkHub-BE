package com.tenten.linkhub.domain.notification.controller.dto;

import com.tenten.linkhub.domain.notification.model.NotificationType;

public record SpaceInviteNotificationGetApiResponse(
        Long notificationId,
        Long invitingMemberId,
        Long spaceId,
        String invitingMemberName,
        String spaceName,
        NotificationType notificationType,
        boolean isAccepted
) {
}
