package com.tenten.linkhub.domain.space.controller.dto.comment;

public record RootCommentFindApiResponse(
        Long commentId,
        String content,
        Long childCount,
        Long memberId,
        String nickname,
        String aboutMe,
        String profileImagePath
) {
}
