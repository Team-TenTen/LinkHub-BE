package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberFollowingsFindResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import java.util.List;
import org.springframework.data.domain.Slice;

public record MemberFollowingsFindApiResponses(
        List<MemberFollowingsFindApiResponse> responses,
        PageMetaData metaData
) {

    public static MemberFollowingsFindApiResponses from(MemberFollowingsFindResponses responses) {
        Slice<MemberFollowingsFindApiResponse> mapResponses = responses.responses()
                .map(r -> new MemberFollowingsFindApiResponse(
                        r.memberId(),
                        r.nickname(),
                        r.aboutMe(),
                        r.profileImagePath(),
                        r.favoriteCategory(),
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