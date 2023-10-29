package com.tenten.linkhub.domain.space.service.dto;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceWithSpaceImage;
import org.springframework.data.domain.Slice;

public record SpacesFindByQueryResponses(Slice<SpacesFindByQueryResponse> responses) {

    public static SpacesFindByQueryResponses from(Slice<SpaceWithSpaceImage> spaces){
        Slice<SpacesFindByQueryResponse> mapResponses = spaces.map(s -> new SpacesFindByQueryResponse(
                s.space().getId(),
                s.space().getSpaceName(),
                s.space().getDescription() == null ? "" : s.space().getDescription(),
                s.space().getCategory(),
                s.space().getIsVisible(),
                s.space().getIsComment(),
                s.space().getIsLinkSummarizable(),
                s.space().getIsReadMarkEnabled(),
                s.space().getViewCount(),
                s.space().getScrapCount(),
                s.space().getFavoriteCount(),
                s.spaceImage().getPath()
        ));

        return new SpacesFindByQueryResponses(mapResponses);
    }

}
