package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record MySpacesFindApiResponses(
        List<MySpacesFindApiResponse> responses,
        PageMetaData metaData
) {
    public static MySpacesFindApiResponses from(SpacesFindByQueryResponses responses){
        Slice<MySpacesFindApiResponse> mapResponses = responses.responses()
                .map(r -> new MySpacesFindApiResponse(
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

        return new MySpacesFindApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }

}
