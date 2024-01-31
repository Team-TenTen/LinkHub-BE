package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.common.SpaceCursorSlice;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickName;

import java.util.Objects;

public record SpacesFindWithCursorResponses(SpaceCursorSlice<SpacesFindByQueryResponse> responses) {

    public static SpacesFindWithCursorResponses from(SpaceCursorSlice<SpaceAndSpaceImageOwnerNickName> response){
        SpaceCursorSlice<SpacesFindByQueryResponse> mapResponses = response.map(s -> new SpacesFindByQueryResponse(
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
                s.ownerNickName()
        ));

        return new SpacesFindWithCursorResponses(mapResponses);
    }

}
