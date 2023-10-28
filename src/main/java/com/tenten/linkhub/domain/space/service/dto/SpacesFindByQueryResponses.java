package com.tenten.linkhub.domain.space.service.dto;

import com.tenten.linkhub.domain.space.model.space.Space;
import org.springframework.data.domain.Slice;

public record SpacesFindByQueryResponses(Slice<SpacesFindByQueryResponse> responses) {

    public static SpacesFindByQueryResponses from(Slice<Space> spaces){
        Slice<SpacesFindByQueryResponse> mapResponses = spaces.map(s -> new SpacesFindByQueryResponse(
                s.getId(),
                s.getSpaceName(),
                s.getDescription() == null ? "" : s.getDescription(),
                s.getCategory(),
                s.getIsVisible(),
                s.getIsComment(),
                s.getIsLinkSummarizable(),
                s.getIsReadMarkEnabled(),
                s.getViewCount(),
                s.getScrapCount(),
                s.getFavoriteCount()
        ));

        return new SpacesFindByQueryResponses(mapResponses);
    }

}
