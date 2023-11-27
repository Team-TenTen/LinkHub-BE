package com.tenten.linkhub.domain.notification.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SpaceInviteNotificationGetApiRequest(
        @Schema(title = "페이지 번호", example = "0")
        Integer pageNumber,

        @Schema(title = "페이지 크기", example = "10")
        Integer pageSize
) {
}
