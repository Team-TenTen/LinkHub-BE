package com.tenten.linkhub.domain.member.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailVerificationApiRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String code
) {
}
