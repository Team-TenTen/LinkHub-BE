package com.tenten.linkhub.domain.space.controller.dto.invitation;

import com.tenten.linkhub.domain.space.model.space.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SpaceInvitationApiRequest(
        @Schema(title = "이메일", example = "frong@gmail.com")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 빈 값이 들어올 수 없습니다.") String email,

        @Schema(title = "스페이스 아이디", example = "2")
        @NotNull(message = "스페이스 아이디는 빈 값이 들어올 수 없습니다.") Long spaceId,

        @Schema(title = "권한", example = "CAN_EDIT")
        @NotNull(message = "권한은 빈 값이 들어올 수 없습니다.") Role role
) {
}
