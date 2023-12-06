package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.notification.service.NotificationService;
import com.tenten.linkhub.domain.space.facade.dto.SpaceInvitationFacadeRequest;
import com.tenten.linkhub.domain.space.facade.mapper.SpaceInvitationFacadeMapper;
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
    private final SpaceInvitationFacadeMapper mapper;
    private final MemberService memberService;

    @Transactional
    public Long invite(SpaceInvitationFacadeRequest request) {
        Long memberId = memberService.findMemberIdByEmail(request.email());

        spaceInvitationService.validateSpaceInvitation(memberId, request.myMemberId(), request.spaceId());

        Long notificationId = notificationService.createNotification(mapper.toNotificationCreateRequest(request, memberId));

        spaceInvitationService.createInvitation(mapper.toSpaceInvitationRequest(request, notificationId, memberId));

        return notificationId;
    }

    @Transactional
    public Long acceptSpaceInvitation(SpaceInvitationAcceptRequest request) {
        Long spaceId = spaceInvitationService.acceptSpaceInvitation(request);

        notificationService.changeIsCheckedAsTrue(request.notificationId(), request.memberId());

        return spaceId;
    }

}
