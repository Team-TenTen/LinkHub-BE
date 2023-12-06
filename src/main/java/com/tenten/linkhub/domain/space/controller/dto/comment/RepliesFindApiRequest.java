package com.tenten.linkhub.domain.space.controller.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;

public record RepliesFindApiRequest(
        @Schema(title = "페이지 번호", example = "0")
        Integer pageNumber,

        @Schema(title = "페이지 크기", example = "10")
        Integer pageSize
) {
}
