package com.tenten.linkhub.domain.space.repository.favorite;

import com.tenten.linkhub.domain.space.model.space.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {

    Boolean existsByMemberIdAndSpaceId(Long memberId, Long spaceId);

    Optional<Favorite> findByMemberIdAndSpaceId(Long memberId, Long spaceId);
}
