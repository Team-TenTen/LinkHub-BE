package com.tenten.linkhub.domain.notification.service.mapper;

import com.tenten.linkhub.domain.notification.repository.dto.NotificationGetQueryCondition;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInvitationGetRequest;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationGetQueryCondition toQueryCondition(SpaceInvitationGetRequest request) {
        return new NotificationGetQueryCondition(
                request.memberId(),
                request.pageable()
        );
    }
}
