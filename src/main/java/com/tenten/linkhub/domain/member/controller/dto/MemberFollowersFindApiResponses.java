package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberFollowersFindResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record MemberFollowersFindApiResponses(
        List<MemberFollowersFindApiResponse> responses,
        PageMetaData metaData
) {
    public static MemberFollowersFindApiResponses from(MemberFollowersFindResponses responses) {
        Slice<MemberFollowersFindApiResponse> mapResponses = responses.responses()
                .map(r -> new MemberFollowersFindApiResponse(
                        r.memberId(),
                        r.nickname(),
                        r.profileImagePath(),
                        r.isFollowing()));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new MemberFollowersFindApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }

}
