package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SpaceCreateApiRequest(

        @Schema(title = "스페이스 이름", example = "java 개발자 출근길 보기 좋은 글 모음")
        @NotBlank(message = "스페이스 이름은 빈 값이 들어올 수 없습니다.") String spaceName,

        @Schema(title = "스페이스 소개글", example = "java 개발자 하루 30분 매일 하나씩 보기 좋은 글 모음입니다.")
        String description,

        @Schema(title = "스페이스 카테고리", example = "KNOWLEDGE_ISSUE_CAREER")
        @NotNull(message = "스페이스의 카테고리는 null이 될 수 없습니다.") Category category,

        @Schema(title = "스페이스 공개 여부", example = "true")
        @NotNull(message = "공개 여부는 null이 들어올 수 없습니다.") Boolean isVisible,

        @Schema(title = "스페이스 댓글 작성 여부", example = "true")
        @NotNull(message = "댓글 작성 여부는 null이 들어올 수 없습니다.") Boolean isComment,

        @Schema(title = "스페이스 링크 요약 가능 여부", example = "true")
        @NotNull(message = "3줄 요약 여부는 null이 들어올 수 없습니다.") Boolean isLinkSummarizable,

        @Schema(title = "스페이스 읽음 처리 표시 여부", example = "true")
        @NotNull(message = "읽음 처리 여부는 null이 들어올 수 없습니다.") Boolean isReadMarkEnabled,

        @Schema(title = "스페이스 생성 유저id (임시 컬럼으로 이후 헤더를 통해 받을 예정)", example = "1")
        Long memberId
) {
}
