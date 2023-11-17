package com.tenten.linkhub.domain.space.repository.favorite;

import com.tenten.linkhub.domain.space.model.space.Favorite;
import com.tenten.linkhub.global.exception.DataNotFoundException;
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

    @Override
    public Favorite getBySpaceIdAndMemberId(Long spaceId, Long memberId) {
        return favoriteJpaRepository.findByMemberIdAndSpaceId(memberId, spaceId)
                .orElseThrow(() -> new DataNotFoundException("해당 memberId와 spaceId를 가진 Favorite을 찾을 수 없습니다."));
    }

    @Override
    public Long deleteById(Long favoriteId) {
        favoriteJpaRepository.deleteById(favoriteId);
        return favoriteId;
    }

}
