package com.tenten.linkhub.domain.space.repository.link;

import com.tenten.linkhub.domain.space.model.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LinkJpaRepository extends JpaRepository<Link, Long> {

    @Query("SELECT l from Link l WHERE l.id = :linkId AND l.isDeleted = false")
    Optional<Link> findById(Long linkId);

    Long countLinkBySpaceIdAndIsDeletedFalse(Long spaceId);

}
