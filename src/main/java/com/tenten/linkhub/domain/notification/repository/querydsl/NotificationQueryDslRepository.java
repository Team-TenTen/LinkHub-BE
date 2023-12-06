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
                        notification.senderId,
                        invitation.space.id,
                        member.nickname,
                        space.spaceName,
                        notification.notificationType,
                        invitation.isAccepted
                ))
                .from(notification)
                .leftJoin(invitation).on(invitation.notificationId.eq(notification.id))
                .leftJoin(invitation.space, space)
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

    public boolean existsByRecipientIdAndSenderIdAndSpaceId(Long memberId, Long myMemberId, Long spaceId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(notification)
                .join(invitation).on(invitation.notificationId.eq(notification.id))
                .where(equalsRecipientIdAndSenderIdAndSpaceId(memberId, myMemberId, spaceId))
                .fetchFirst();
        return fetchOne != null;

    }

    private BooleanExpression equalsRecipientIdAndSenderIdAndSpaceId(Long recipientId, Long senderId, Long spaceId) {
        BooleanExpression recipientCondition = notification.recipientId.eq(recipientId);
        BooleanExpression senderCondition = notification.senderId.eq(senderId);
        BooleanExpression spaceConditon = invitation.space.id.eq(spaceId);

        return recipientCondition.and(senderCondition).and(spaceConditon);
    }

    BooleanExpression checkMemberJoinCondition(Long memberId) {
        BooleanExpression joinMemberCondition = notification.senderId.eq(member.id);
        BooleanExpression isSameMemberId = notification.recipientId.eq(memberId);

        return joinMemberCondition.and(isSameMemberId);
    }

}
