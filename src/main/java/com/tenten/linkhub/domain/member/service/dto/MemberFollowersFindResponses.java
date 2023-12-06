package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.repository.dto.FollowDTO;
import org.springframework.data.domain.Slice;

public record MemberFollowersFindResponses(Slice<MemberFollowersFindResponse> responses) {

    public static MemberFollowersFindResponses from(Slice<FollowDTO> followDTOs) {
        Slice<MemberFollowersFindResponse> memberFollowersFindResponses = followDTOs.map(f -> new MemberFollowersFindResponse(
                f.follow().getFollowing().getId(),
                f.follow().getFollowing().getNickname(),
                f.follow().getFollowing().getAboutMe(),
                f.follow().getFollowing().retrieveProfileImages().isEmpty() ? null
                        : f.follow().getFollowing().retrieveProfileImages().get(0).getPath(),
                f.isFollowing()
        ));

        return new MemberFollowersFindResponses(memberFollowersFindResponses);
    }
}