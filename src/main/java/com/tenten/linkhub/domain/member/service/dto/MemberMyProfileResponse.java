package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.space.model.category.Category;

public record MemberMyProfileResponse(
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

    public static MemberMyProfileResponse from(Member member, Long followerCount, Long followingCount) {
        return new MemberMyProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getAboutMe(),
                member.getNewsEmail(),
                followingCount,
                followerCount,
                member.retrieveFavoriteCategories().isEmpty() ? null
                        : member.retrieveFavoriteCategories().get(0).getCategory(),
                member.retrieveProfileImages().isEmpty() ? null : member.retrieveProfileImages().get(0).getPath(),
                member.getIsSubscribed()
        );
    }
}
