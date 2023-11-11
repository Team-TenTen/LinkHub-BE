package com.tenten.linkhub.domain.member.repository.follow;

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

}
