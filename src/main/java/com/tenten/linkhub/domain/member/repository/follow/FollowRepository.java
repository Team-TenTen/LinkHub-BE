package com.tenten.linkhub.domain.member.repository.follow;

import com.tenten.linkhub.domain.member.model.Follow;
import com.tenten.linkhub.domain.member.repository.dto.FollowDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface FollowRepository {

    Long countFollowers(Long memberId);

    Long countFollowing(Long memberId);

    Boolean existsByMemberIdAndMyMemberId(Long memberId, Long myMemberId);

    Follow save(Follow follow);

    Optional<Follow> findByMemberIdAndMyMemberId(Long memberId, Long myMemberId);

    void delete(Follow follow);

    Slice<FollowDTO> findFollowingsOfTargetUserWithMyMemberFollowingStatus(Long memberId, Long myMemberId, PageRequest pageRequest);

    Slice<FollowDTO> findFollowersOfTargetUserWithMyMemberFollowingStatus(Long memberId, Long myMemberId, PageRequest pageRequest);
}
