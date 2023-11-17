package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.space.model.category.Category;
import java.time.LocalDateTime;

public record MemberFollowersFindResponse(
        Long memberId,
        String nickname,
        String aboutMe,
        String profileImagePath,
        Category favoriteCategory,
        Boolean isFollowing
) {
}