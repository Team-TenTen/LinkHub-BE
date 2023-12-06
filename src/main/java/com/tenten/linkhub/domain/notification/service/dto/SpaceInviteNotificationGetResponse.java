package com.tenten.linkhub.domain.notification.service.dto;

import com.tenten.linkhub.domain.notification.model.NotificationType;

public record SpaceInviteNotificationGetResponse(
        Long notificationId,
        Long invitingMemberId,
        Long spaceId,
        String invitingMemberName,
        String spaceName,
        NotificationType notificationType,
        boolean isAccepted
) {
}
