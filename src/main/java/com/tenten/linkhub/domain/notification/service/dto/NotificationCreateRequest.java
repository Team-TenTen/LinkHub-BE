package com.tenten.linkhub.domain.notification.service.dto;

public record NotificationCreateRequest(
        Long memberId,
        Long myMemberId
) {
}
