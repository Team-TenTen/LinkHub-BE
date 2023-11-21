package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.space.model.category.Category;

public record MemberProfileResponse(
        Long memberId,
        String nickname,
        String aboutMe,
        String newsEmail,
        Long followingCount,
        Long followerCount,
        Category favoriteCategory,
        String profileImagePath,
        Boolean isSubscribed,
        Boolean isModifiable,
        Boolean isFollowing
) {

    public static MemberProfileResponse from(Member member, Long followerCount, Long followingCount, Boolean isModifiable, Boolean isFollowing) {
        return new MemberProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getAboutMe(),
                member.getNewsEmail(),
                followingCount,
                followerCount,
                member.retrieveFavoriteCategories().isEmpty() ? null
                        : member.retrieveFavoriteCategories().get(0).getCategory(),
                member.retrieveProfileImages().isEmpty() ? null : member.retrieveProfileImages().get(0).getPath(),
                member.getIsSubscribed(),
                isModifiable,
                isFollowing
        );
    }
}
