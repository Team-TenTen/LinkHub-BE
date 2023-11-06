package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
