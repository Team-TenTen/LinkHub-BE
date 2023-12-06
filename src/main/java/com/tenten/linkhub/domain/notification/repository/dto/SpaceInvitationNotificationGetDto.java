package com.tenten.linkhub.domain.notification.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.notification.model.NotificationType;

public record SpaceInvitationNotificationGetDto(
        Long notificationId,
        Long invitingMemberId,
        Long spaceId,
        String invitingMemberName,
        String spaceName,
        NotificationType notificationType,
        boolean isAccepted
) {
    @QueryProjection
    public SpaceInvitationNotificationGetDto(Long notificationId,
                                             Long invitingMemberId,
                                             Long spaceId,
                                             String invitingMemberName,
                                             String spaceName,
                                             NotificationType notificationType,
                                             boolean isAccepted) {
        this.notificationId = notificationId;
        this.invitingMemberId = invitingMemberId;
        this.spaceId = spaceId;
        this.invitingMemberName = invitingMemberName;
        this.spaceName = spaceName;
        this.notificationType = notificationType;
        this.isAccepted = isAccepted;
    }
}
