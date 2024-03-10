package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.common.CursorPageMataData;
import com.tenten.linkhub.domain.space.common.SpaceCursorSlice;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindWithCursorResponses;

import java.util.List;

public record PublicSpaceFindWithFilterApiResponses(
        List<PublicSpaceFindWithFilterApiResponse> responses,
        CursorPageMataData metaData
) {
    public static PublicSpaceFindWithFilterApiResponses from(SpacesFindWithCursorResponses responses) {
        SpaceCursorSlice<PublicSpaceFindWithFilterApiResponse> mapResponses = responses.responses()
                .map(r -> new PublicSpaceFindWithFilterApiResponse(
                        r.spaceId(),
                        r.spaceName(),
                        r.description(),
                        r.category(),
                        r.viewCount(),
                        r.scrapCount(),
                        r.favoriteCount(),
                        r.spaceImagePath(),
                        r.ownerNickName()));

        CursorPageMataData cursorPageMataData = new CursorPageMataData(
                mapResponses.getLastFavoriteCount(),
                mapResponses.getLastId(),
                mapResponses.getPageSize(),
                mapResponses.getHasNext()
        );

        return new PublicSpaceFindWithFilterApiResponses(
                mapResponses.getContent(),
                cursorPageMataData);
    }
}
