package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.handler.dto.SpaceIncreaseFavoriteCountDto;
import com.tenten.linkhub.domain.space.model.space.Favorite;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.favorite.FavoriteRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.favorite.SpaceRegisterInFavoriteResponse;
import com.tenten.linkhub.domain.space.service.mapper.FavoriteMapper;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final SpaceRepository spaceRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final FavoriteMapper mapper;

    @Transactional
    public SpaceRegisterInFavoriteResponse createFavorite(Long spaceId, Long memberId) {
        checkSpaceExistenceOrThrowException(spaceId);
        eventPublisher.publishEvent(new SpaceIncreaseFavoriteCountDto(spaceId));

        Space space = spaceRepository.getById(spaceId);

        Favorite favorite = mapper.toFavorite(space, memberId);
        Favorite savedFavorite = favoriteRepository.save(favorite);

        return SpaceRegisterInFavoriteResponse.of(savedFavorite.getId(), spaceId);
    }

    private void checkSpaceExistenceOrThrowException(Long spaceId) {
        if (!spaceRepository.existsById(spaceId)){
            throw new DataNotFoundException("해당 space를 찾을 수 없습니다.");
        }
    }

}
