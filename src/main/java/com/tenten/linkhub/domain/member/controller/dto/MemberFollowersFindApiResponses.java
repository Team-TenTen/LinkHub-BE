package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberFollowersFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowingsFindResponses;
import com.tenten.linkhub.domain.space.controller.dto.comment.RepliesFindApiResponse;
import com.tenten.linkhub.domain.space.facade.dto.RepliesAndMemberInfoResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import java.util.List;
import org.springframework.data.domain.Slice;

public record MemberFollowersFindApiResponses(
        List<MemberFollowersFindApiResponse> responses,
        PageMetaData metaData
) {
    public static MemberFollowersFindApiResponses from(MemberFollowersFindResponses responses) {
        Slice<MemberFollowersFindApiResponse> mapResponses = responses.responses()
                .map(r -> new MemberFollowersFindApiResponse(
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

        return new MemberFollowersFindApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }

}
