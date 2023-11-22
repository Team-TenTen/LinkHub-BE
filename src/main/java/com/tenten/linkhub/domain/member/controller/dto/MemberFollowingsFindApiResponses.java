package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberFollowingsFindResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record MemberFollowingsFindApiResponses(
        List<MemberFollowingsFindApiResponse> responses,
        PageMetaData metaData
) {

    public static MemberFollowingsFindApiResponses from(MemberFollowingsFindResponses responses) {
        Slice<MemberFollowingsFindApiResponse> mapResponses = responses.responses()
                .map(r -> new MemberFollowingsFindApiResponse(
                        r.memberId(),
                        r.nickname(),
                        r.profileImagePath(),
                        r.isFollowing()));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new MemberFollowingsFindApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }
}