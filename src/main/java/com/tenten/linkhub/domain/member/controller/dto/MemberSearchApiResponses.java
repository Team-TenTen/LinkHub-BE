package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberSearchResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record MemberSearchApiResponses(
        List<MemberSearchApiResponse> responses,
        PageMetaData metaData
) {

    public static MemberSearchApiResponses from(MemberSearchResponses responses) {
        Slice<MemberSearchApiResponse> mapResponses = responses.responses()
                .map(r -> new MemberSearchApiResponse(
                        r.memberId(),
                        r.aboutMe(),
                        r.nickname(),
                        r.memberImagePath(),
                        r.isFollowing()
                ));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new MemberSearchApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }

}
