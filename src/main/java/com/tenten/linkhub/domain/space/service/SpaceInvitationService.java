package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Invitation;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.invitation.SpaceInvitationRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.invitation.SpaceInvitationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SpaceInvitationService {
    private final SpaceInvitationRepository spaceInvitationRepository;
    private final SpaceRepository spaceRepository;

    public Invitation createInvitation(SpaceInvitationRequest request) {
        Space space = spaceRepository.getById(request.spaceId());

        Invitation invitation = new Invitation(
                space,
                request.role(),
                request.notificationId(),
                request.memberId()
        );

        return spaceInvitationRepository.save(invitation);
    }
}
