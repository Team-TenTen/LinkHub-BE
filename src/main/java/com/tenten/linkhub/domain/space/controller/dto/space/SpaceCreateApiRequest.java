package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SpaceCreateApiRequest(
        @NotBlank(message = "스페이스 이름은 빈 값이 들어올 수 없습니다.") String spaceName,
        String description,
        @NotNull(message = "스페이스의 카테고리는 null이 될 수 없습니다.") Category category,
        @NotNull(message = "공개 여부는 null이 들어올 수 없습니다.") Boolean isVisible,
        @NotNull(message = "댓글 작성 여부는 null이 들어올 수 없습니다.") Boolean isComment,
        @NotNull(message = "3줄 요약 여부는 null이 들어올 수 없습니다.") Boolean isLinkSummarizable,
        @NotNull(message = "읽음 처리 여부는 null이 들어올 수 없습니다.") Boolean isReadMarkEnabled,
        Long memberId
) {
}
