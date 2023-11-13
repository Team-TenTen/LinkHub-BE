package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.facade.dto.SpaceMemberDetailInfo;
import com.tenten.linkhub.domain.space.model.category.Category;

import java.util.List;

public record SpaceDetailGetByIdApiResponse(
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
        Boolean isOwner,
        Boolean isCanEdit,
        Boolean hasFavorite,
        List<SpaceMemberDetailInfo> memberDetailInfos
) {
    public static SpaceDetailGetByIdApiResponse from(SpaceDetailGetByIdFacadeResponse response){
        return new SpaceDetailGetByIdApiResponse(
                response.spaceId(),
                response.spaceName(),
                response.description(),
                response.category(),
                response.isVisible(),
                response.isComment(),
                response.isLinkSummarizable(),
                response.isReadMarkEnabled(),
                response.viewCount(),
                response.scrapCount(),
                response.favoriteCount(),
                response.spaceImagePath(),
                response.isOwner(),
                response.isCanEdit(),
                response.hasFavorite(),
                response.memberDetailInfos()
        );
    }

}
