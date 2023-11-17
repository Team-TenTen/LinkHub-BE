package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.service.dto.comment.RepliesFindResponse;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Slice;

public record MemberFollowersFindResponses(Slice<MemberFollowersFindResponse> responses) {

    public static MemberFollowersFindResponses from(
            Slice<Member> followersSlice,
            List<Boolean> isMyMemberFollowingList
    ) {
        Iterator<Boolean> followingIterator = isMyMemberFollowingList.iterator();

        Slice<MemberFollowersFindResponse> memberFollowersFindResponses = followersSlice.map(f -> {
            Boolean isFollowing = followingIterator.hasNext() && followingIterator.next();

            return new MemberFollowersFindResponse(
                    f.getId(),
                    f.getNickname(),
                    f.getAboutMe(),
                    f.retrieveProfileImages().isEmpty() ? null : f.retrieveProfileImages().get(0).getPath(),
                    f.retrieveFavoriteCategories().isEmpty() ? null
                            : f.retrieveFavoriteCategories().get(0).getCategory(),
                    isFollowing
            );
        });

        return new MemberFollowersFindResponses(memberFollowersFindResponses);
    }

}