package com.tenten.linkhub.domain.notification.service.dto;

import com.tenten.linkhub.domain.notification.repository.dto.SpaceInvitationNotificationGetDto;
import org.springframework.data.domain.Slice;

public record SpaceInvitationGetResponses(
        Slice<SpaceInvitationGetResponse> responses
) {
    public static SpaceInvitationGetResponses from(Slice<SpaceInvitationNotificationGetDto> notificationGetDtos) {
        Slice<SpaceInvitationGetResponse> responseList = notificationGetDtos.map(
                n -> new SpaceInvitationGetResponse(
                        n.notificationId(),
                        n.invitingMemberId(),
                        n.spaceId(),
                        n.invitingMemberName(),
                        n.spaceName(),
                        n.notificationType(),
                        n.isAccepted()
                )
        );

        return new SpaceInvitationGetResponses(responseList);
    }
}
