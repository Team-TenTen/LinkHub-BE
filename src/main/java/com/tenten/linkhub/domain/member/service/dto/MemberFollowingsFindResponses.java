package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.repository.dto.FollowDTO;
import org.springframework.data.domain.Slice;

public record MemberFollowingsFindResponses(Slice<MemberFollowingFindResponse> responses) {

    public static MemberFollowingsFindResponses from(Slice<FollowDTO> followDTOs) {
        Slice<MemberFollowingFindResponse> memberFollowingFindResponses = followDTOs.map(f -> new MemberFollowingFindResponse(
                f.follow().getFollower().getId(),
                f.follow().getFollower().getNickname(),
                f.follow().getFollower().retrieveProfileImages().isEmpty() ? null : f.follow().getFollower().retrieveProfileImages().get(0).getPath(),
                f.isFollowing()
        ));

        return new MemberFollowingsFindResponses(memberFollowingFindResponses);
    }
}