package com.tenten.linkhub.domain.notification.controller.dto;

public record UncheckedNotificationsCountApiResponse(
        Long unCheckedNotificationCount
) {
    public static UncheckedNotificationsCountApiResponse from(Long unCheckedNotificationCount) {
        return new UncheckedNotificationsCountApiResponse(unCheckedNotificationCount);
    }
}
