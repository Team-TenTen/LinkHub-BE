package com.tenten.linkhub.domain.space.repository.invitation;

import com.tenten.linkhub.domain.space.model.space.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvitationJpaRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByNotificationId(Long notificationId);

    @Modifying
    @Query("DELETE FROM Invitation i WHERE i.notificationId = :notificationId AND i.memberId = :memberId")
    void deleteByNotificationId(Long notificationId, Long memberId);
}
