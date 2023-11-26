package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.notification.service.NotificationService;
import com.tenten.linkhub.domain.space.facade.dto.SpaceInvitationFacadeRequest;
import com.tenten.linkhub.domain.space.facade.mapper.SpaceInvitationFacadeMapper;
import com.tenten.linkhub.domain.space.service.SpaceInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SpaceInvitationFacade {
    private final SpaceInvitationService spaceInvitationService;
    private final NotificationService notificationService;
    private final SpaceInvitationFacadeMapper mapper;

    @Transactional
    public Long invite(SpaceInvitationFacadeRequest request) {
        Long notificationId = notificationService.createNotification(mapper.toNotificationCreateRequest(request));

        spaceInvitationService.createInvitation(mapper.toSpaceInvitationRequest(request, notificationId));

        return notificationId;
    }
}
