package com.tenten.linkhub.domain.notification.controller.dto;

import com.tenten.linkhub.domain.notification.service.dto.SpaceInvitationGetResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record SpaceInvitationGetApiResponses(
        List<SpaceInvitationGetApiResponse> responses,
        PageMetaData pageMetaData
) {
    public static SpaceInvitationGetApiResponses from(SpaceInvitationGetResponses responses) {
        Slice<SpaceInvitationGetApiResponse> invitationResponses = responses.responses()
                .map(i -> new SpaceInvitationGetApiResponse(
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

        return new SpaceInvitationGetApiResponses(
                invitationResponses.getContent(),
                pageMetaData
        );
    }
}
