package com.tenten.linkhub.domain.space.controller.dto.comment;

import com.tenten.linkhub.domain.space.facade.dto.RepliesAndMemberInfoResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record RepliesFindApiResponses(
        List<RepliesFindApiResponse> responses,
        PageMetaData metaData
) {
    public static RepliesFindApiResponses from(RepliesAndMemberInfoResponses responses){
        Slice<RepliesFindApiResponse> mapResponses = responses.responses()
                .map(r -> new RepliesFindApiResponse(
                        r.commentId(),
                        r.content(),
                        r.createdAt(),
                        r.updatedAt(),
                        r.memberId(),
                        r.nickname(),
                        r.aboutMe(),
                        r.profileImagePath()
                ));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new RepliesFindApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }

}
