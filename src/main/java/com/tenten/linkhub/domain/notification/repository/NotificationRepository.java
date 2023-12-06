package com.tenten.linkhub.domain.notification.repository;

import com.tenten.linkhub.domain.notification.model.Notification;
import com.tenten.linkhub.domain.notification.repository.dto.NotificationGetQueryCondition;
import com.tenten.linkhub.domain.notification.repository.dto.SpaceInvitationNotificationGetDto;
import org.springframework.data.domain.Slice;

public interface NotificationRepository {
    Notification getById(Long notificationId);

    Slice<SpaceInvitationNotificationGetDto> getInviteNotifications(NotificationGetQueryCondition condition);

    Notification save(Notification notification);

    boolean existsByMemberIdAndMyMemberIdAndSpaceId(Long memberId, Long myMemberId, Long spaceId);

    void deleteByIdAndRecipientId(Long notificationId, Long memberId);

    Long countUncheckedNotificationsByRecipientId(Long memberId);
}
