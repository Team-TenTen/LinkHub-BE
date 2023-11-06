package com.tenten.linkhub.domain.space.controller.dto;

import com.tenten.linkhub.domain.space.model.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public record MySpacesFindApiRequest(
        @Schema(title = "페이지 번호", example = "0")
        Integer pageNumber,

        @Schema(title = "페이지 크기", example = "10")
        Integer pageSize,

        @Schema(title = "검색 키워드", example = "첫번째")
        String keyWord,

        @Schema(title = "검색 필터", example = "KNOWLEDGE_ISSUE_CAREER")
        Category filter
) {
}
