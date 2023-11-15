package com.tenten.linkhub.domain.space.service.dto.favorite;

public record SpaceRegisterInFavoriteResponse(Long favoriteId, Long registeredSpaceId) {

    public static SpaceRegisterInFavoriteResponse of(Long favoriteId, Long registeredSpaceId){
        return new SpaceRegisterInFavoriteResponse(favoriteId, registeredSpaceId);
    }

}