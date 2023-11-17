package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Member;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Slice;

public record MemberFollowingsFindResponses(Slice<MemberFollowingFindResponse> responses) {

    public static MemberFollowingsFindResponses from(
            Slice<Member> followingsSlice,
            List<Boolean> isMyMemberFollowingList
    ) {
        Iterator<Boolean> followingIterator = isMyMemberFollowingList.iterator();

        Slice<MemberFollowingFindResponse> memberFollowingFindResponses = followingsSlice.map(f -> {
            Boolean isFollowing = followingIterator.hasNext() && followingIterator.next();

            return new MemberFollowingFindResponse(
                    f.getId(),
                    f.getNickname(),
                    f.getAboutMe(),
                    f.retrieveProfileImages().isEmpty() ? null : f.retrieveProfileImages().get(0).getPath(),
                    f.retrieveFavoriteCategories().isEmpty() ? null
                            : f.retrieveFavoriteCategories().get(0).getCategory(),
                    isFollowing
            );
        });

        return new MemberFollowingsFindResponses(memberFollowingFindResponses);
    }
}