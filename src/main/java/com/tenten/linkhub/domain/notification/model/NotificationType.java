package com.tenten.linkhub.domain.notification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    COMMENT("댓글"),

    FOLLOW("팔로우"),

    INVITATION("초대");

    private final String name;
}
