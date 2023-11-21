package com.tenten.linkhub.domain.member.repository.follow;

import com.tenten.linkhub.domain.member.model.Follow;
import com.tenten.linkhub.domain.member.repository.dto.FollowDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DefaultFollowRepository implements FollowRepository {

    private final FollowJpaRepository followJpaRepository;

    public DefaultFollowRepository(FollowJpaRepository followJpaRepository) {
        this.followJpaRepository = followJpaRepository;
    }

    @Override
    public Long countFollowers(Long memberId) {
        return followJpaRepository.countFollowers(memberId);
    }

    @Override
    public Long countFollowing(Long memberId) {
        return followJpaRepository.countFollowing(memberId);
    }

    @Override
    public Boolean existsByMemberIdAndMyMemberId(Long memberId, Long myMemberId) {
        return followJpaRepository.existsByMemberIdAndMyMemberId(memberId, myMemberId);
    }

    @Override
    public Follow save(Follow follow) {
        return followJpaRepository.save(follow);
    }

    @Override
    public Optional<Follow> findByMemberIdAndMyMemberId(Long memberId, Long myMemberId) {
        return followJpaRepository.findByMemberIdAndMyMemberId(memberId, myMemberId);
    }

    @Override
    public void delete(Follow follow) {
        followJpaRepository.delete(follow);
    }

    @Override
    public Slice<FollowDTO> findFollowingsOfTargetUserWithMyMemberFollowingStatus(Long memberId, Long myMemberId, PageRequest pageRequest) {
        return followJpaRepository.findFollowingsOfTargetUserWithMyMemberFollowingStatus(memberId, myMemberId, pageRequest);
    }

    @Override
    public Slice<FollowDTO> findFollowersOfTargetUserWithMyMemberFollowingStatus(Long memberId, Long myMemberId, PageRequest pageRequest) {
        return followJpaRepository.findFollowersOfTargetUserWithMyMemberFollowingStatus(memberId, myMemberId, pageRequest);
    }

}
