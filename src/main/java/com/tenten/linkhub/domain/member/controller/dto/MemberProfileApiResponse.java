package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.space.model.category.Category;

public record MemberProfileApiResponse(
        Long memberId,
        String nickname,
        String aboutMe,
        String newsEmail,
        Long followingCount,
        Long followerCount,
        Category favoriteCategory,
        String profileImagePath,
        Boolean isSubscribed
) {

}
