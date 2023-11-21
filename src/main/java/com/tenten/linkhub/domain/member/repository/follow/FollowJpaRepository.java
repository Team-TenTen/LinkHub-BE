package com.tenten.linkhub.domain.member.repository.follow;

import com.tenten.linkhub.domain.member.model.Follow;
import com.tenten.linkhub.domain.member.repository.dto.FollowDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FollowJpaRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :memberId")
    Long countFollowers(Long memberId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :memberId")
    Long countFollowing(Long memberId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Follow f WHERE f.follower.id = :memberId AND f.following.id = :myMemberId")
    Boolean existsByMemberIdAndMyMemberId(@Param("memberId") Long memberId, @Param("myMemberId") Long myMemberId);

    @Query("SELECT f FROM Follow f WHERE f.follower.id = :memberId AND f.following.id = :myMemberId")
    Optional<Follow> findByMemberIdAndMyMemberId(Long memberId, Long myMemberId);

    @Query("SELECT f FROM Follow f JOIN Member m ON m.id = f.following.id WHERE f.following.id = :memberId ORDER BY f.createdAt ASC")
    Slice<Follow> findByFollowingId(Long memberId, PageRequest pageRequest);

    @Query("SELECT f FROM Follow f JOIN Member m ON m.id = f.follower.id WHERE f.follower.id = :memberId ORDER BY f.createdAt ASC")
    Slice<Follow> findByFollowerId(Long memberId, PageRequest pageRequest);

    @Query("SELECT f.follower.id FROM Follow f WHERE f.following.id = :followingId AND f.follower.id IN :followedMemberIds")
    Set<Long> findFollowedMemberIdsByFollowingId(@Param("followingId") Long myMemberId, List<Long> followedMemberIds);

    @Query("SELECT new com.tenten.linkhub.domain.member.repository.dto.FollowDTO(f, CASE WHEN fMy.following.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Follow f " +
            "LEFT JOIN Follow fMy ON f.follower = fMy.follower AND fMy.following.id = :myMemberId " +
            "LEFT JOIN FETCH f.follower " +
            "LEFT JOIN FETCH f.follower.profileImages " +
            "WHERE f.following.id = :memberId AND f.follower.isDeleted = false AND f.following.isDeleted = false " +
            "ORDER BY f.createdAt ASC")
    Slice<FollowDTO> findFollowingsOfTargetUserWithMyMemberFollowingStatus(Long memberId, Long myMemberId, PageRequest pageRequest);

    @Query("SELECT new com.tenten.linkhub.domain.member.repository.dto.FollowDTO(f, CASE WHEN fMy.following.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Follow f " +
            "LEFT JOIN Follow fMy ON f.following = fMy.follower AND fMy.following.id = :myMemberId " +
            "LEFT JOIN FETCH f.following " +
            "LEFT JOIN FETCH f.following.profileImages " +
            "WHERE f.follower.id = :memberId AND f.following.isDeleted = false AND f.follower.isDeleted = false " +
            "ORDER BY f.createdAt ASC")
    Slice<FollowDTO> findFollowersOfTargetUserWithMyMemberFollowingStatus(Long memberId, Long myMemberId, PageRequest pageRequest);
}
