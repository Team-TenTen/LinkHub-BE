package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import com.tenten.linkhub.global.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record SpacesFindByQueryApiResponses(
        List<SpacesFindByQueryApiResponse> responses,
        PageMetaData metaData
) {

    public static SpacesFindByQueryApiResponses from(SpacesFindByQueryResponses responses) {
        Slice<SpacesFindByQueryApiResponse> mapResponses = responses.responses()
                .map(r -> new SpacesFindByQueryApiResponse(
                        r.spaceId(),
                        r.spaceName(),
                        r.description(),
                        r.category(),
                        r.viewCount(),
                        r.scrapCount(),
                        r.favoriteCount()));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new SpacesFindByQueryApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }

}
