package com.tenten.linkhub.domain.space.repository.favorite;

import com.tenten.linkhub.domain.space.model.space.Favorite;
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

    @Override
    public Favorite save(Favorite favorite) {
        return favoriteJpaRepository.save(favorite);
    }

}
