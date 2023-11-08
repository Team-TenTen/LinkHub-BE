package com.tenten.linkhub.domain.space.controller.dto.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentCreateApiRequest(
        @NotNull(message = "댓글의 content는 null이 될 수 없습니다.")
        @Size(max = 1000, message = "댓글의 content는 1000자 이상 될 수 없습니다.")
        String content
) {
}
