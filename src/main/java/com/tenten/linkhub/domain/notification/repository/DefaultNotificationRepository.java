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
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    @Override
    public boolean existsByMemberIdAndMyMemberIdAndSpaceId(Long memberId, Long myMemberId, Long spaceId) {
        return notificationQueryDslRepository.existsByRecipientIdAndSenderIdAndSpaceId(memberId, myMemberId, spaceId);
    }

    @Override
    public void deleteByIdAndRecipientId(Long notificationId, Long memberId) {
        notificationJpaRepository.deleteByIdAndRecipientId(notificationId, memberId);
    }

    @Override
    public Long countUncheckedNotificationsByRecipientId(Long memberId) {
        return notificationJpaRepository.countByRecipientIdAndIsCheckedFalse(memberId);
    }

    @Override
    public Slice<SpaceInvitationNotificationGetDto> getInviteNotifications(NotificationGetQueryCondition condition) {
        return notificationQueryDslRepository.getSpaceInvitationNotifications(condition);
    }

}
