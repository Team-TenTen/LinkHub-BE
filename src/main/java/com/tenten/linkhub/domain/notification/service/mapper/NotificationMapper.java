package com.tenten.linkhub.domain.notification.service.mapper;

import com.tenten.linkhub.domain.notification.repository.dto.NotificationGetQueryCondition;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInviteNotificationGetRequest;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationGetQueryCondition toQueryCondition(SpaceInviteNotificationGetRequest request) {
        return new NotificationGetQueryCondition(
                request.memberId(),
                request.pageable()
        );
    }
}
