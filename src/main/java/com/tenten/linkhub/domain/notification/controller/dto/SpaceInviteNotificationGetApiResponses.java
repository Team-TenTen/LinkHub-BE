package com.tenten.linkhub.domain.notification.controller.dto;

import com.tenten.linkhub.domain.notification.service.dto.SpaceInviteNotificationGetResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record SpaceInviteNotificationGetApiResponses(
        List<SpaceInviteNotificationGetApiResponse> responses,
        PageMetaData pageMetaData
) {
    public static SpaceInviteNotificationGetApiResponses from(SpaceInviteNotificationGetResponses responses) {
        Slice<SpaceInviteNotificationGetApiResponse> invitationResponses = responses.responses()
                .map(i -> new SpaceInviteNotificationGetApiResponse(
                        i.notificationId(),
                        i.invitingMemberId(),
                        i.spaceId(),
                        i.invitingMemberName(),
                        i.spaceName(),
                        i.notificationType(),
                        i.isAccepted()
                ));

        PageMetaData pageMetaData = new PageMetaData(
                invitationResponses.hasNext(),
                invitationResponses.getSize(),
                invitationResponses.getNumber());

        return new SpaceInviteNotificationGetApiResponses(
                invitationResponses.getContent(),
                pageMetaData
        );
    }
}
