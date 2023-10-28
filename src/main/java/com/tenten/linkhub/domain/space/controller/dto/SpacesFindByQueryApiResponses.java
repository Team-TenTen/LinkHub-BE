package com.tenten.linkhub.domain.space.controller.dto;

import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import org.springframework.data.domain.Slice;

public record SpacesFindByQueryApiResponses(Slice<SpacesFindByQueryApiResponse> responses) {

    public static SpacesFindByQueryApiResponses from(SpacesFindByQueryResponses responses){
        Slice<SpacesFindByQueryApiResponse> mapResponses = responses.responses()
                .map(r -> new SpacesFindByQueryApiResponse(
                        r.spaceId(),
                        r.spaceName(),
                        r.description(),
                        r.category(),
                        r.viewCount(),
                        r.scrapCount(),
                        r.favoriteCount()));

        return new SpacesFindByQueryApiResponses(mapResponses);
    }
}
