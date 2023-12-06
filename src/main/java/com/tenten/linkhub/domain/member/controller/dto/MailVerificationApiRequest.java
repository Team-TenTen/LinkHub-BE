package com.tenten.linkhub.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailVerificationApiRequest(
        @Schema(title = "이메일", example = "fordevmin@gmail.com")
        @NotBlank(message = "메일은 빈 값이 들어올 수 없습니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @Schema(title = "인증번호", example = "123456")
        @NotBlank(message = "인증번호는 빈 값이 들어올 수 없습니다.")
        String code
) {
}
