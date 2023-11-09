package com.tenten.linkhub.domain.space.facade.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record LinkCreateFacadeRequest(
        @NotBlank(message = "URL은 빈 값이 들어올 수 없습니다.")
        @URL(message = "적절한 URL 형식이 아닙니다.")
        String url,

        @NotBlank(message = "title은 빈 값이 들어올 수 없습니다.")
        String title,

        String tag
) {
}
