package com.tenten.linkhub.domain.notification.service;

import com.tenten.linkhub.domain.notification.model.Notification;
import com.tenten.linkhub.domain.notification.repository.NotificationRepository;
import com.tenten.linkhub.domain.notification.repository.dto.SpaceInvitationNotificationGetDto;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInvitationGetRequest;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInvitationGetResponses;
import com.tenten.linkhub.domain.notification.service.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public void changeIsCheckedAsTrue(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.getById(notificationId);

        notification.changeIsCheckedAsTrue(memberId);
    }

    public SpaceInvitationGetResponses getSpaceInvitations(SpaceInvitationGetRequest request) {
        Slice<SpaceInvitationNotificationGetDto> notificationGetDtos = notificationRepository.getInviteNotifications(notificationMapper.toQueryCondition(request));
        return SpaceInvitationGetResponses.from(notificationGetDtos);
    }
}
