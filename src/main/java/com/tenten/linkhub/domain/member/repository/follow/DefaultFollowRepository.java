package com.tenten.linkhub.domain.member.repository.follow;

import com.tenten.linkhub.domain.member.model.Follow;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

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
    public Slice<Follow> findByFollowingId(Long memberId, PageRequest pageRequest) {
        return followJpaRepository.findByFollowingId(memberId, pageRequest);
    }

    @Override
    public Slice<Follow> findByFollowerId(Long memberId, PageRequest pageRequest) {
        return followJpaRepository.findByFollowerId(memberId, pageRequest);
    }

    @Override
    public Set<Long> findFollowedMemberIdsByFollowingId(Long myMemberId, List<Long> followedMemberIds) {
        return followJpaRepository.findFollowedMemberIdsByFollowingId(myMemberId, followedMemberIds);
    }

}
