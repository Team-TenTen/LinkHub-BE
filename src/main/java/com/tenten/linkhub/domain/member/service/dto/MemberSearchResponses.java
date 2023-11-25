package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.repository.dto.MemberWithProfileImageAndFollowingStatus;
import org.springframework.data.domain.Slice;

public record MemberSearchResponses(Slice<MemberSearchResponse> responses) {

    public static MemberSearchResponses from(Slice<MemberWithProfileImageAndFollowingStatus> response) {
        Slice<MemberSearchResponse> mapResponses = response.map(m -> new MemberSearchResponse(
                m.member().getId(),
                m.member().getAboutMe(),
                m.member().getNickname(),
                m.member().retrieveProfileImages().isEmpty() ? null
                        : m.member().retrieveProfileImages().get(0).getPath(),
                m.isFollowing()
        ));

        return new MemberSearchResponses(mapResponses);
    }

}
