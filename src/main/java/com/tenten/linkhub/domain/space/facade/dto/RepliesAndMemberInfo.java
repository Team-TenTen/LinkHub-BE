package com.tenten.linkhub.domain.space.facade.dto;

import java.time.LocalDateTime;

public record RepliesAndMemberInfo(
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
