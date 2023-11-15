package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import org.springframework.data.domain.Slice;

import java.util.Objects;

public record PublicSpacesFindByQueryResponses(Slice<SpacesFindByQueryResponse> responses) {

    public static PublicSpacesFindByQueryResponses from(Slice<Space> spaces){
        Slice<SpacesFindByQueryResponse> mapResponses = spaces.map(s -> new SpacesFindByQueryResponse(
                s.getId(),
                s.getSpaceName(),
                Objects.isNull(s.getDescription()) ? "" : s.getDescription(),
                s.getCategory(),
                s.getIsVisible(),
                s.getIsComment(),
                s.getIsLinkSummarizable(),
                s.getIsReadMarkEnabled(),
                s.getViewCount(),
                s.getScrapCount(),
                s.getFavoriteCount(),
                s.getSpaceImages().isEmpty() ? null : s.getSpaceImages().get(0).getPath()
        ));

        return new PublicSpacesFindByQueryResponses(mapResponses);
    }

}
