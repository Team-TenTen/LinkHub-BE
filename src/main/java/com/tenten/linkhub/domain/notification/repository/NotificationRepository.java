package com.tenten.linkhub.domain.notification.repository;

import com.tenten.linkhub.domain.notification.model.Notification;

public interface NotificationRepository {
    Notification save(Notification notification);
}
