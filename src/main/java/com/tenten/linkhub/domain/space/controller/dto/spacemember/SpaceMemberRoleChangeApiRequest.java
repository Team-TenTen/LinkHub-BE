package com.tenten.linkhub.domain.space.controller.dto.spacemember;

import com.tenten.linkhub.domain.space.model.space.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record SpaceMemberRoleChangeApiRequest(
        @Schema(title = "권한 수정할 memberId", example = "2")
        Long targetMemberId,

        @Schema(title = "수정할 권한", example = "CAN_EDIT")
        Role role
) {
}
