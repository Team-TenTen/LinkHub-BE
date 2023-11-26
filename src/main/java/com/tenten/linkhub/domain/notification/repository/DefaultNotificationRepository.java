package com.tenten.linkhub.domain.notification.repository;

import com.tenten.linkhub.domain.notification.model.Notification;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultNotificationRepository implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;

    public DefaultNotificationRepository(NotificationJpaRepository notificationJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
    }

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }
}
