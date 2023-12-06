package com.tenten.linkhub.domain.space.controller.dto.favorite;

import com.tenten.linkhub.domain.space.service.dto.favorite.FavoriteSpacesFindResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record MyFavoriteSpacesFindApiResponses(
        List<MyFavoriteSpacesFindApiResponse> responses,
        PageMetaData metaData
) {
    public static MyFavoriteSpacesFindApiResponses from(FavoriteSpacesFindResponses responses) {
        Slice<MyFavoriteSpacesFindApiResponse> mapResponses = responses.responses()
                .map(r -> new MyFavoriteSpacesFindApiResponse(
                        r.spaceId(),
                        r.spaceName(),
                        r.description(),
                        r.category(),
                        r.viewCount(),
                        r.scrapCount(),
                        r.favoriteCount(),
                        r.spaceImagePath(),
                        r.ownerNickName()));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new MyFavoriteSpacesFindApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }

}
