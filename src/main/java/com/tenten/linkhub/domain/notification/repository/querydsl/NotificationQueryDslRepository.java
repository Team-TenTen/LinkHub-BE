package com.tenten.linkhub.domain.notification.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.notification.repository.dto.NotificationGetQueryCondition;
import com.tenten.linkhub.domain.notification.repository.dto.QSpaceInvitationNotificationGetDto;
import com.tenten.linkhub.domain.notification.repository.dto.SpaceInvitationNotificationGetDto;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.member.model.QMember.member;
import static com.tenten.linkhub.domain.notification.model.QNotification.notification;
import static com.tenten.linkhub.domain.space.model.space.QInvitation.invitation;
import static com.tenten.linkhub.domain.space.model.space.QSpace.space;

@Repository
public class NotificationQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public NotificationQueryDslRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;

    }

    public Slice<SpaceInvitationNotificationGetDto> getSpaceInvitationNotifications(NotificationGetQueryCondition condition) {
        List<SpaceInvitationNotificationGetDto> notificationGetDtos = jpaQueryFactory
                .select(new QSpaceInvitationNotificationGetDto(
                        notification.id,
                        invitation.memberId,
                        invitation.space.id,
                        member.nickname,
                        space.spaceName,
                        notification.notificationType,
                        invitation.isAccepted
                ))
                .from(notification)
                .join(invitation).on(invitation.notificationId.eq(notification.id))
                .join(invitation.space, space)
                .join(member).on(checkMemberJoinCondition(condition.memberId()))
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (notificationGetDtos.size() > condition.pageable().getPageSize()) {
            notificationGetDtos.remove(condition.pageable().getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(notificationGetDtos, condition.pageable(), hasNext);

    }

    BooleanExpression checkMemberJoinCondition(Long memberId) {
        BooleanExpression joinMemberCondition = invitation.memberId.eq(member.id);
        BooleanExpression isSameMemberId = invitation.memberId.eq(memberId);

        return joinMemberCondition.and(isSameMemberId);
    }

}
