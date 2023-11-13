package com.tenten.linkhub.domain.space.repository.favorite;

import org.springframework.stereotype.Repository;

@Repository
public class DefaultFavoriteRepository implements FavoriteRepository{

    private final FavoriteJpaRepository favoriteJpaRepository;

    public DefaultFavoriteRepository(FavoriteJpaRepository favoriteJpaRepository) {
        this.favoriteJpaRepository = favoriteJpaRepository;
    }

    @Override
    public Boolean isExist(Long memberId, Long spaceId) {
        return favoriteJpaRepository.existsByMemberIdAndSpaceId(memberId, spaceId);
    }

}
