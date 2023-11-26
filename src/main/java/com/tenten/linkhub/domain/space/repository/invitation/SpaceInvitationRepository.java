package com.tenten.linkhub.domain.space.repository.invitation;

import com.tenten.linkhub.domain.space.model.space.Invitation;

public interface SpaceInvitationRepository {
    Invitation save(Invitation invitation);
}
