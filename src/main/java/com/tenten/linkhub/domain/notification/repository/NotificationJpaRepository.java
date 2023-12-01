package com.tenten.linkhub.domain.notification.repository;

import com.tenten.linkhub.domain.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    Boolean existsByRecipientIdAndSenderId(Long recipientId, Long senderId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.id = :notificationId AND n.recipientId = :memberId")
    void deleteByIdAndRecipientId(Long notificationId, Long memberId);

    Long countByRecipientIdAndCheckedFalse(Long recipientId);
}
