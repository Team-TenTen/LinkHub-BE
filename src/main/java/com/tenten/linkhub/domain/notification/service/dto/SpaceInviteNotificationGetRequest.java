package com.tenten.linkhub.domain.notification.service.dto;

import org.springframework.data.domain.Pageable;

public record SpaceInviteNotificationGetRequest(
        Pageable pageable,
        Long memberId
) {
}
