package com.tenten.linkhub.domain.notification.service;

import com.tenten.linkhub.domain.notification.model.Notification;
import com.tenten.linkhub.domain.notification.repository.NotificationRepository;
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

}
