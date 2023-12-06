package com.tenten.linkhub.domain.space.controller.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RootCommentCreateApiRequest(
        @Schema(title = "댓글 내용", example = "스페이스의 링크들이 너무 알차요!!")
        @NotBlank(message = "댓글의 content는 null이나 빈 문자열이 될 수 없습니다.")
        @Size(max = 1000, message = "댓글의 content는 1000자 이상 될 수 없습니다.")
        String content
) {
}
