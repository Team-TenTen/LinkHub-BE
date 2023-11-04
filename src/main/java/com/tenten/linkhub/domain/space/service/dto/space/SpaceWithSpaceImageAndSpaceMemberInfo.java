package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Space;

import java.util.List;
import java.util.Objects;

public record SpaceWithSpaceImageAndSpaceMemberInfo(
        Long spaceId,
        String spaceName,
        String description,
        Category category,
        Boolean isVisible,
        Boolean isComment,
        Boolean isLinkSummarizable,
        Boolean isReadMarkEnabled,
        Long viewCount,
        Long scrapCount,
        Long favoriteCount,
        String spaceImagePath,
        String spaceImageName,
        Boolean isOwner,
        List<SpaceMemberInfo> spaceMemberInfos
) {
    public static SpaceWithSpaceImageAndSpaceMemberInfo of(Space space, Boolean isOwner) {
        List<SpaceMemberInfo> spaceMemberInfos = space.getSpaceMembers().stream()
                .map(sm -> new SpaceMemberInfo(sm.getMemberId(), sm.getRole()))
                .toList();

        return new SpaceWithSpaceImageAndSpaceMemberInfo(
                space.getId(),
                space.getSpaceName(),
                space.getDescription(),
                space.getCategory(),
                space.getIsVisible(),
                space.getIsComment(),
                space.getIsLinkSummarizable(),
                space.getIsReadMarkEnabled(),
                space.getViewCount(),
                space.getScrapCount(),
                space.getFavoriteCount(),
                Objects.isNull(space.getSpaceImages().get(0)) ? null: space.getSpaceImages().get(0).getPath(),
                Objects.isNull(space.getSpaceImages().get(0)) ? null: space.getSpaceImages().get(0).getName(),
                isOwner,
                spaceMemberInfos
        );
    }

}
