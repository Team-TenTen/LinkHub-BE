package com.tenten.linkhub.domain.space.facade.dto;

import com.tenten.linkhub.domain.space.model.link.Color;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

public record LinkCreateFacadeRequest(
        @NotBlank(message = "URL은 빈 값이 들어올 수 없습니다.")
        @URL(message = "적절한 URL 형식이 아닙니다.")
        String url,

        @NotBlank(message = "title은 빈 값이 들어올 수 없습니다.")
        String title,

        @Pattern(regexp = "^(?!\\s*$).+", message = "태그는 비어있거나 공백만 있을 수 없습니다.")
        String tag,

        @NotNull(message = "링크 tag의 색은 빈 값이 들어올 수 없습니다.")
        Color color
) {
}
