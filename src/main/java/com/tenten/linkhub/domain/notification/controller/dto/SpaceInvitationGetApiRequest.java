package com.tenten.linkhub.domain.notification.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SpaceInvitationGetApiRequest(
        @Schema(title = "페이지 번호", example = "1")
        Integer pageNumber,

        @Schema(title = "페이지 크기", example = "10")
        Integer pageSize
) {
}
