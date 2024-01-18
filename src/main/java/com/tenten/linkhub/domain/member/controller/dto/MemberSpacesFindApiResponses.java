package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.member.facade.dto.MemberSpacesFindByQueryFacadeResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record MemberSpacesFindApiResponses(
        List<MemberSpacesFindApiResponse> responses,
        PageMetaData metaData
) {
    public static MemberSpacesFindApiResponses from(MemberSpacesFindByQueryFacadeResponses responses){
        Slice<MemberSpacesFindApiResponse> mapResponses = responses.responses()
                .map(r -> new MemberSpacesFindApiResponse(
                        r.spaceId(),
                        r.spaceName(),
                        r.description(),
                        r.category(),
                        r.viewCount(),
                        r.scrapCount(),
                        r.favoriteCount(),
                        r.spaceImagePath(),
                        r.ownerNickName(),
                        r.isVisible()));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new MemberSpacesFindApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }

}
