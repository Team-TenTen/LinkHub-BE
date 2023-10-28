package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import org.springframework.data.domain.Slice;

import java.util.List;

public record SpacesFindByQueryApiResponses(
        List<SpacesFindByQueryApiResponse> responses,
        Boolean hasNext,
        Integer pageSize,
        Integer pageNumber) {

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

        return new SpacesFindByQueryApiResponses(
                mapResponses.getContent(),
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());
    }

}
