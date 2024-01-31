package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public record PublicSpacesFindWithFilterApiRequest(
        @Schema(title = "마지막 favoriteCount", example = "1")
        Long lastFavoriteCount,

        @Schema(title = "마지막 spaceId", example = "1")
        Long lastSpaceId,

        @Schema(title = "페이지 크기", example = "10")
        Integer pageSize,

        @Schema(title = "정렬 조건 (컬럼명)", example = "created_at")
        String sort,

        @Schema(title = "검색 필터", example = "KNOWLEDGE_ISSUE_CAREER")
        Category filter
) {
}
