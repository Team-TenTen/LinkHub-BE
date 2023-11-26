package com.tenten.linkhub.domain.space.repository.invitation;

import com.tenten.linkhub.domain.space.model.space.Invitation;

public interface InvitationRepository {
    Invitation getByNotificationId(Long notificationId);

    Invitation save(Invitation invitation);
}
