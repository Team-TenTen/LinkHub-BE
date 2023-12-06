package com.tenten.linkhub.domain.member.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailVerificationRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String code
) {
}
