package com.tenten.linkhub.domain.space.controller.dto.comment;

import java.time.LocalDateTime;

public record RootCommentFindApiResponse(
        Long commentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long childCount,
        Long memberId,
        String nickname,
        String aboutMe,
        String profileImagePath,
        Boolean isModifiable
) {
}
