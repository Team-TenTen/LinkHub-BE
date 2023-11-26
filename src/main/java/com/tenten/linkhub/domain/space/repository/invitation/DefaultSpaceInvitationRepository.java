package com.tenten.linkhub.domain.space.repository.invitation;

import com.tenten.linkhub.domain.space.model.space.Invitation;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultSpaceInvitationRepository implements SpaceInvitationRepository {
    private final SpaceInvitationJpaRepository spaceInvitationJpaRepository;

    public DefaultSpaceInvitationRepository(SpaceInvitationJpaRepository spaceInvitationJpaRepository) {
        this.spaceInvitationJpaRepository = spaceInvitationJpaRepository;
    }

    @Override
    public Invitation save(Invitation invitation) {
        return spaceInvitationJpaRepository.save(invitation);
    }
}
