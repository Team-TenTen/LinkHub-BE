package com.tenten.linkhub.domain.member.repository.follow;

import com.tenten.linkhub.domain.member.model.Follow;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

public interface FollowRepository {

    Long countFollowers(Long memberId);

    Long countFollowing(Long memberId);

    Boolean existsByMemberIdAndMyMemberId(Long memberId, Long myMemberId);

    Follow save(Follow follow);

    Optional<Follow> findByMemberIdAndMyMemberId(Long memberId, Long myMemberId);

    void delete(Follow follow);

    Slice<Follow> findByFollowingId(Long memberId, PageRequest pageRequest);

    Slice<Follow> findByFollowerId(Long memberId, PageRequest pageRequest);

    Set<Long> findFollowedMemberIdsByFollowingId(Long myMemberId, List<Long> followedMemberIds);
}
