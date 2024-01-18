package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImage;
import org.springframework.data.domain.Slice;

import java.util.Objects;

public record SpacesFindByQueryResponses(Slice<SpacesFindByQueryResponse> responses) {

    public static SpacesFindByQueryResponses from(Slice<SpaceAndSpaceImage> response){
        Slice<SpacesFindByQueryResponse> mapResponses = response.map(s -> new SpacesFindByQueryResponse(
                s.space().getId(),
                s.space().getSpaceName(),
                Objects.isNull(s.space().getDescription()) ? "" : s.space().getDescription(),
                s.space().getCategory(),
                s.space().getIsVisible(),
                s.space().getIsComment(),
                s.space().getIsLinkSummarizable(),
                s.space().getIsReadMarkEnabled(),
                s.space().getViewCount(),
                s.space().getScrapCount(),
                s.space().getFavoriteCount(),
                s.spaceImages().isEmpty() ? null : s.spaceImages().get(0).getPath(),
                s.space().getMemberId()
        ));

        return new SpacesFindByQueryResponses(mapResponses);
    }

}
