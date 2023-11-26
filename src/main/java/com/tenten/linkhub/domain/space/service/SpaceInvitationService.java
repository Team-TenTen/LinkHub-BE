package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Invitation;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.invitation.InvitationRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.invitation.SpaceInvitationAcceptRequest;
import com.tenten.linkhub.domain.space.service.dto.invitation.SpaceInvitationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SpaceInvitationService {

    private final InvitationRepository invitationRepository;
    private final SpaceRepository spaceRepository;

    @Transactional
    public Long acceptSpaceInvitation(SpaceInvitationAcceptRequest request) {
        Invitation invitation = invitationRepository.getByNotificationId(request.notificationId());
        invitation.changeIsAcceptedAsTrue(request.memberId());

        Space space = invitation.getSpace();
        space.addSpaceMember(new SpaceMember(request.memberId(), invitation.getRole()));

        return space.getId();
    }

    public Invitation createInvitation(SpaceInvitationRequest request) {
        Space space = spaceRepository.getById(request.spaceId());

        Invitation invitation = new Invitation(
                space,
                request.role(),
                request.memberId(),
                request.notificationId()
        );

        return invitationRepository.save(invitation);
    }
}
