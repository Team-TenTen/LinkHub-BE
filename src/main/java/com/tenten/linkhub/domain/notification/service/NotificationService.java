package com.tenten.linkhub.domain.notification.service;

import com.tenten.linkhub.domain.notification.service.dto.NotificationCreateRequest;

public interface NotificationService {

    Long createNotification(NotificationCreateRequest request);
}
