package com.tenten.linkhub.domain.space.controller.dto.link;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record LinkCreateApiRequest(
        @Schema(title = "링크 URL", example = "https://mideveloperni.tistory.com/")
        @NotBlank(message = "URL은 빈 값이 들어올 수 없습니다.")
        @Size(max = 2083, message = "링크의 url은 최대 2083자 입니다.")
        @URL(message = "적절한 URL 형식이 아닙니다.")
        String url,

        @Schema(title = "링크 title", example = "개발 블로그 1")
        @Size(max = 100, message = "title은 최대 100자 입니다.")
        @NotBlank(message = "title은 빈 값이 들어올 수 없습니다.")
        String title,

        @Schema(title = "링크 tagName", example = "개발", description = "링크 생성 시 태그가 필요하면 tagName 필드를 넣고, 태그 없이 링크를 생성한다면 아예 태그 필드를 제외하여 요청을 보내야 합니다.")
        @Pattern(regexp = "^(?!\\s*$).+", message = "태그는 비어있거나 공백만 있을 수 없습니다.")
        @Size(max = 30, message = "tag는 최대 30자 입니다.")
        String tagName,

        @Schema(title = "링크 tag의 색", example = "red", description = "미리 정의된 8가지 중 한가지 색을 입력해주세요. 단, 링크 수정 시 태그를 넣지 않는다면 json에 아예 태그 필드를 제외하여 요청을 보내야 합니다.")
        @Pattern(regexp = "^(?!\\s*$).+", message = "태그 컬러는 비어있거나 공백만 있을 수 없습니다.")
        String color
) {
}
