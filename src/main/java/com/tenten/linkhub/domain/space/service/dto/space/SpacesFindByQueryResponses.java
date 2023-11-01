package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import org.springframework.data.domain.Slice;

import java.util.Objects;

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
                s.getFavoriteCount(),
                Objects.isNull(s.getSpaceImages().get(0)) ? null : s.getSpaceImages().get(0).getPath()
        ));

        return new SpacesFindByQueryResponses(mapResponses);
    }

}
