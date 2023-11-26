package com.tenten.linkhub.domain.notification.service.dto;

import com.tenten.linkhub.domain.notification.repository.dto.SpaceInvitationNotificationGetDto;
import org.springframework.data.domain.Slice;

public record SpaceInviteNotificationGetResponses(
        Slice<SpaceInviteNotificationGetResponse> responses
) {
    public static SpaceInviteNotificationGetResponses from(Slice<SpaceInvitationNotificationGetDto> notificationGetDtos) {
        Slice<SpaceInviteNotificationGetResponse> responseList = notificationGetDtos.map(
                n -> new SpaceInviteNotificationGetResponse(
                        n.notificationId(),
                        n.invitingMemberId(),
                        n.spaceId(),
                        n.invitingMemberName(),
                        n.spaceName(),
                        n.notificationType(),
                        n.isAccepted()
                )
        );

        return new SpaceInviteNotificationGetResponses(responseList);
    }
}
