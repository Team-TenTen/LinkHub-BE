package com.tenten.linkhub.domain.notification.repository.dto;

import org.springframework.data.domain.Pageable;

public record NotificationGetQueryCondition(
        Long memberId,
        Pageable pageable
) {
}
