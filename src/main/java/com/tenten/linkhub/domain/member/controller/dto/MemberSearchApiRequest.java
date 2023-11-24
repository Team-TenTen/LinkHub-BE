package com.tenten.linkhub.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberSearchApiRequest(
        @Schema(title = "페이지 번호", example = "0")
        Integer pageNumber,

        @Schema(title = "페이지 크기", example = "10")
        Integer pageSize,

        @Schema(title = "검색 키워드", example = "첫번째")
        String keyword
) {

}
