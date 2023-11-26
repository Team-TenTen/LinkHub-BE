package com.tenten.linkhub.domain.notification.service;

import com.tenten.linkhub.domain.notification.model.Notification;
import com.tenten.linkhub.domain.notification.model.NotificationType;
import com.tenten.linkhub.domain.notification.repository.NotificationRepository;
import com.tenten.linkhub.domain.notification.service.dto.NotificationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultNotificationService implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public Long createNotification(NotificationCreateRequest request) {

        Notification notification = new Notification(
                request.memberId(),
                request.myMemberId(),
                NotificationType.INVITATION
        );

        return notificationRepository.save(notification).getId();
    }
}
