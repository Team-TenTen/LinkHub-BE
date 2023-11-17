package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Favorite;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.favorite.FavoriteRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.favorite.SpaceRegisterInFavoriteResponse;
import com.tenten.linkhub.domain.space.service.mapper.FavoriteMapper;
import com.tenten.linkhub.global.exception.DataDuplicateException;
import com.tenten.linkhub.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final SpaceRepository spaceRepository;
    private final FavoriteMapper mapper;

    @Transactional
    public SpaceRegisterInFavoriteResponse createFavorite(Long spaceId, Long memberId) {
        Space space = spaceRepository.getById(spaceId);
        space.validateVisibilityAndMembership(memberId);

        checkDuplicateFavorite(spaceId, memberId);

        Favorite favorite = mapper.toFavorite(space, memberId);
        Favorite savedFavorite = favoriteRepository.save(favorite);

        spaceRepository.increaseFavoriteCount(spaceId);

        return SpaceRegisterInFavoriteResponse.of(
                savedFavorite.getId(),
                space.getFavoriteCount() + 1);
    }

    @Transactional
    public Long cancelFavoriteSpace(Long spaceId, Long memberId) {
        Favorite favorite = favoriteRepository.getBySpaceIdAndMemberId(spaceId, memberId);
        favorite.validateOwnership(memberId);

        Long deletedFavoriteId = favoriteRepository.deleteById(favorite.getId());

        spaceRepository.decreaseFavoriteCount(spaceId);

        return deletedFavoriteId;
    }

    private void checkDuplicateFavorite(Long spaceId, Long memberId) {
        if (favoriteRepository.isExist(memberId, spaceId)){
            throw new DataDuplicateException(ErrorCode.DUPLICATE_FAVORITE);
        }
    }

}
