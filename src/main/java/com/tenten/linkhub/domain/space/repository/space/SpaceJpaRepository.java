package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpaceJpaRepository extends JpaRepository<Space, Long> {

    @Query("SELECT s " +
            "FROM Space s " +
            "LEFT JOIN FETCH s.spaceMembers.spaceMemberList sm " +
            "WHERE s.id = :spaceId AND s.isDeleted = false ")
    Optional<Space> findSpaceJoinSpaceMemberById(Long spaceId);

    @Query("SELECT s FROM Space s WHERE s.id = :spaceId AND s.isDeleted = false ")
    Optional<Space> findById(Long spaceId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE spaces SET favorite_count = favorite_count + 1 WHERE id = :spaceId AND is_deleted = false ", nativeQuery = true)
    void increaseFavoriteCount(Long spaceId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE spaces SET favorite_count = favorite_count - 1 WHERE id = :spaceId AND is_deleted = false ", nativeQuery = true)
    void decreaseFavoriteCount(Long spaceId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE spaces SET scrap_count = scrap_count + 1 WHERE id = :spaceId AND is_deleted = false ", nativeQuery = true)
    void increaseScrapCount(Long spaceId);
}
