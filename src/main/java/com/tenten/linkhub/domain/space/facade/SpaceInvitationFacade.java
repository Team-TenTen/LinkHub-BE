package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.notification.service.NotificationService;
import com.tenten.linkhub.domain.space.facade.dto.SpaceInvitationFacadeRequest;
import com.tenten.linkhub.domain.space.service.SpaceInvitationService;
import com.tenten.linkhub.domain.space.service.dto.invitation.SpaceInvitationAcceptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SpaceInvitationFacade {

    private final SpaceInvitationService spaceInvitationService;
    private final NotificationService notificationService;

    public Long invite(SpaceInvitationFacadeRequest request) {
        return null;
    }

    @Transactional
    public Long acceptSpaceInvitation(SpaceInvitationAcceptRequest request) {
        Long spaceId = spaceInvitationService.acceptSpaceInvitation(request);

        notificationService.changeIsCheckedAsTrue(request.notificationId(), request.memberId());

        return spaceId;
    }

}
