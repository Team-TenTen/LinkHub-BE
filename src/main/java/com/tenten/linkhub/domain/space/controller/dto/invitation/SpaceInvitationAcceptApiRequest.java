package com.tenten.linkhub.domain.space.controller.dto.invitation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SpaceInvitationAcceptApiRequest(
        @Schema(title = "초대ID", example = "1")
        @NotNull(message = "알림ID는 null이 들어올 수 없습니다.")
        Long notificationId
) {
}
