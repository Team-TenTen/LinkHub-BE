package com.tenten.linkhub.domain.notification.controller.mapper;

import com.tenten.linkhub.domain.notification.service.dto.SpaceInviteNotificationGetRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface NotificationApiMapper {
    SpaceInviteNotificationGetRequest toSpaceInvitationGetRequest(Pageable pageable, Long memberId);
}
