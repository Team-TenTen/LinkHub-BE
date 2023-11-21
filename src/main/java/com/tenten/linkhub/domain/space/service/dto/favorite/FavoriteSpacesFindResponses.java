package com.tenten.linkhub.domain.space.service.dto.favorite;

import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickName;
import org.springframework.data.domain.Slice;

import java.util.Objects;

public record FavoriteSpacesFindResponses(Slice<FavoriteSpacesFindResponse> responses) {

    public static FavoriteSpacesFindResponses from(Slice<SpaceAndSpaceImageOwnerNickName> response){
        Slice<FavoriteSpacesFindResponse> mapResponses = response.map(s -> new FavoriteSpacesFindResponse(
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

        return new FavoriteSpacesFindResponses(mapResponses);
    }

}
