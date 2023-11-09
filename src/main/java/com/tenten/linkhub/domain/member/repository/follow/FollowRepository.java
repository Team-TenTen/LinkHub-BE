package com.tenten.linkhub.domain.member.repository.follow;

public interface FollowRepository {

    Long countFollowers(Long memberId);

    Long countFollowing(Long memberId);
}
