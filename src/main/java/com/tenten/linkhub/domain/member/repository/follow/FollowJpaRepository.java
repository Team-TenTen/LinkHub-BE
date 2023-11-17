package com.tenten.linkhub.domain.member.repository.follow;

import com.tenten.linkhub.domain.member.model.Follow;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowJpaRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :memberId")
    Long countFollowers(Long memberId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :memberId")
    Long countFollowing(Long memberId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Follow f WHERE f.follower.id = :memberId AND f.following.id = :myMemberId")
    Boolean existsByMemberIdAndMyMemberId(@Param("memberId") Long memberId, @Param("myMemberId") Long myMemberId);

    @Query("SELECT f FROM Follow f WHERE f.follower.id = :memberId AND f.following.id = :myMemberId")
    Optional<Follow> findByMemberIdAndMyMemberId(Long memberId, Long myMemberId);

    @Query("SELECT f FROM Follow f JOIN Member m WHERE f.following.id = :memberId")
    Slice<Follow> findByFollowingId(Long memberId, PageRequest pageRequest);

    @Query("SELECT f FROM Follow f JOIN Member m WHERE f.follower.id = :memberId")
    Slice<Follow> findByFollowerId(Long memberId, PageRequest pageRequest);

    @Query("SELECT f.follower.id FROM Follow f WHERE f.following.id = :followingId AND f.follower.id IN :followedMemberIds")
    Set<Long> findFollowedMemberIdsByFollowingId(@Param("followingId") Long myMemberId, List<Long> followedMemberIds);
}
