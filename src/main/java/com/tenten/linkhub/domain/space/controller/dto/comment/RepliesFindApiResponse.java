package com.tenten.linkhub.domain.space.controller.dto.comment;

import java.time.LocalDateTime;

public record RepliesFindApiResponse(
        Long commentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long memberId,
        String nickname,
        String aboutMe,
        String profileImagePath,
        Long groupNumber,
        Long parentCommentId
) {
}
