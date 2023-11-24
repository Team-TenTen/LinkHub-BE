package com.tenten.linkhub.domain.space.controller.dto.link;

import io.swagger.v3.oas.annotations.media.Schema;

public record LinksGetWithFilterApiRequest(
        @Schema(title = "페이지 번호", example = "1")
        Integer pageNumber,

        @Schema(title = "페이지 크기", example = "10")
        Integer pageSize,

        @Schema(title = "정렬 조건 - (created_at, popular)", example = "created_at")
        String sort,

        @Schema(title = "필터링 하려는 태그의 id", example = "1")
        Long tagId
) {
}
