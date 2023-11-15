package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record SpaceFindWithFilterApiResponses(
        List<SpaceFindWithFilterApiResponse> responses,
        PageMetaData metaData
) {
    public static SpaceFindWithFilterApiResponses from(SpacesFindByQueryResponses responses) {
        Slice<SpaceFindWithFilterApiResponse> mapResponses = responses.responses()
                .map(r -> new SpaceFindWithFilterApiResponse(
                        r.spaceId(),
                        r.spaceName(),
                        r.description(),
                        r.category(),
                        r.viewCount(),
                        r.scrapCount(),
                        r.favoriteCount(),
                        r.spaceImagePath()));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new SpaceFindWithFilterApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }
}
