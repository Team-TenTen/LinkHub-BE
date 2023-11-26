package com.tenten.linkhub.domain.space.facade.mapper;

import com.tenten.linkhub.domain.notification.service.dto.NotificationCreateRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceInvitationFacadeRequest;
import com.tenten.linkhub.domain.space.service.dto.invitation.SpaceInvitationRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceInvitationFacadeMapper {

    NotificationCreateRequest toNotificationCreateRequest(SpaceInvitationFacadeRequest request);

    SpaceInvitationRequest toSpaceInvitationRequest(SpaceInvitationFacadeRequest request, Long notificationId);
}
