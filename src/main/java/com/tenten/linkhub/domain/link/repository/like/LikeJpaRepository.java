package com.tenten.linkhub.domain.link.repository.like;

import com.tenten.linkhub.domain.link.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {
    @Query("SELECT l FROM Like l WHERE l.link.id = :linkId AND l.memberId = :memberId")
    Optional<Like> findByLinkIdAndMemberId(Long linkId, Long memberId);
}
