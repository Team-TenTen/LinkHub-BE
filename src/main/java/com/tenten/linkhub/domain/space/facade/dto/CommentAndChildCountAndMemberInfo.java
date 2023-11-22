package com.tenten.linkhub.domain.space.facade.dto;

import java.time.LocalDateTime;

public record CommentAndChildCountAndMemberInfo(
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
