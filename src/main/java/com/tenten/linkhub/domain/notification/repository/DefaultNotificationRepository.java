package com.tenten.linkhub.domain.notification.repository;

import com.tenten.linkhub.domain.notification.model.Notification;
import com.tenten.linkhub.domain.notification.repository.dto.NotificationGetQueryCondition;
import com.tenten.linkhub.domain.notification.repository.dto.SpaceInvitationNotificationGetDto;
import com.tenten.linkhub.domain.notification.repository.querydsl.NotificationQueryDslRepository;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DefaultNotificationRepository implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationQueryDslRepository notificationQueryDslRepository;

    @Override
    public Notification getById(Long notificationId) {
        return notificationJpaRepository.findById(notificationId)
                .orElseThrow(() -> new DataNotFoundException("해당 Notification을 찾지 못했습니다."));
    }

    @Override
    public Slice<SpaceInvitationNotificationGetDto> getInviteNotifications(NotificationGetQueryCondition condition) {
        return notificationQueryDslRepository.getSpaceInvitationNotifications(condition);
    }

}
