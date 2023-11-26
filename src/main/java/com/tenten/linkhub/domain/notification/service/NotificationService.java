package com.tenten.linkhub.domain.notification.service;

import com.tenten.linkhub.domain.notification.model.Notification;
import com.tenten.linkhub.domain.notification.model.NotificationType;
import com.tenten.linkhub.domain.notification.repository.NotificationRepository;
import com.tenten.linkhub.domain.notification.service.dto.NotificationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void changeIsCheckedAsTrue(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.getById(notificationId);

        notification.changeIsCheckedAsTrue(memberId);
    }

    public Long createNotification(NotificationCreateRequest request) {

        Notification notification = new Notification(
                request.memberId(),
                request.myMemberId(),
                NotificationType.INVITATION
        );

        return notificationRepository.save(notification).getId();
    }
}
