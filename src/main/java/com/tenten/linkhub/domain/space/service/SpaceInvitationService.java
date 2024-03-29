package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.notification.repository.NotificationRepository;
import com.tenten.linkhub.domain.space.model.space.Invitation;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.invitation.InvitationRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.invitation.SpaceInvitationAcceptRequest;
import com.tenten.linkhub.domain.space.service.dto.invitation.SpaceInvitationRequest;

import com.tenten.linkhub.global.exception.DataDuplicateException;
import com.tenten.linkhub.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@Service
public class SpaceInvitationService {

    private final InvitationRepository invitationRepository;
    private final SpaceRepository spaceRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public Long acceptSpaceInvitation(SpaceInvitationAcceptRequest request) {
        Invitation invitation = invitationRepository.getByNotificationId(request.notificationId());
        invitation.changeIsAcceptedAsTrue(request.memberId());

        Space space = invitation.getSpace();
        space.addSpaceMember(new SpaceMember(request.memberId(), invitation.getRole()));

        return space.getId();
    }

    public Long createInvitation(SpaceInvitationRequest request) {
        Space space = spaceRepository.getById(request.spaceId());

        Invitation invitation = new Invitation(
                space,
                request.role(),
                request.memberId(),
                request.notificationId()
        );

        return invitationRepository.save(invitation).getId();
    }

    public void validateSpaceInvitation(Long memberId, Long myMemberId, Long spaceId) {
        //중복 초대 체크
        if (notificationRepository.existsByMemberIdAndMyMemberIdAndSpaceId(memberId, myMemberId, spaceId)) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_NOTIFICATION);
        }

        //방장에게 보낸 초대인지 체크
        Space space = spaceRepository.getById(spaceId);
        if (TRUE.equals(space.isOwner(memberId))) {
            throw new IllegalArgumentException("스페이스의 방장은 멤버로 추가할 수 없습니다.");
        }
    }
}
