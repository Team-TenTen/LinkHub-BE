package com.tenten.linkhub.domain.space.repository.invitation;

import com.tenten.linkhub.domain.space.model.space.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationJpaRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByNotificationId(Long notificationId);
}
