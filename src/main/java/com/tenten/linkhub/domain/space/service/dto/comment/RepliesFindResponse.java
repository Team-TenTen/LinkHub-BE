package com.tenten.linkhub.domain.space.service.dto.comment;

import java.time.LocalDateTime;

public record RepliesFindResponse(
        Long commentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long memberId
) {
}