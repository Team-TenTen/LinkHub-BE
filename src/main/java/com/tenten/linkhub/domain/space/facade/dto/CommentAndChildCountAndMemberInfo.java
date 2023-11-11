package com.tenten.linkhub.domain.space.facade.dto;

public record CommentAndChildCountAndMemberInfo(
        Long commentId,
        String content,
        Long childCount,
        Long memberId,
        String nickname,
        String aboutMe,
        String profileImagePath
) {
}
