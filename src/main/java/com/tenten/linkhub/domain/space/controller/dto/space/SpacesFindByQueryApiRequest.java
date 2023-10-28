package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public record SpacesFindByQueryApiRequest(

        @Schema(title = "검색 키워드", example = "첫번째")
        String keyWord,

        @Schema(title = "검색 필터", example = "ENTER_ART")
        Category filter
) {
}
