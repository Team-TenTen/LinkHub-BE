package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record PublicSpaceFindWithFilterApiResponses(
        List<PublicSpaceFindWithFilterApiResponse> responses,
        PageMetaData metaData
) {
    public static PublicSpaceFindWithFilterApiResponses from(PublicSpacesFindByQueryResponses responses) {
        Slice<PublicSpaceFindWithFilterApiResponse> mapResponses = responses.responses()
                .map(r -> new PublicSpaceFindWithFilterApiResponse(
                        r.spaceId(),
                        r.spaceName(),
                        r.description(),
                        r.category(),
                        r.viewCount(),
                        r.scrapCount(),
                        r.favoriteCount(),
                        r.spaceImagePath()));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new PublicSpaceFindWithFilterApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }
}
