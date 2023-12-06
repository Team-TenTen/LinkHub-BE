package com.tenten.linkhub.domain.member.repository.follow;

import com.tenten.linkhub.domain.member.model.Follow;
import com.tenten.linkhub.domain.member.repository.dto.FollowDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FollowJpaRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :memberId")
    Long countFollowers(Long memberId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :memberId")
    Long countFollowing(Long memberId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Follow f WHERE f.follower.id = :memberId AND f.following.id = :myMemberId")
    Boolean existsByMemberIdAndMyMemberId(Long memberId, Long myMemberId);

    @Query("SELECT f FROM Follow f WHERE f.follower.id = :memberId AND f.following.id = :myMemberId")
    Optional<Follow> findByMemberIdAndMyMemberId(Long memberId, Long myMemberId);

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
