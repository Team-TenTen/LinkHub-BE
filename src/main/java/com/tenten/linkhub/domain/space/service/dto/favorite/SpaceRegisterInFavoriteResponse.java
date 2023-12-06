package com.tenten.linkhub.domain.space.service.dto.favorite;

public record SpaceRegisterInFavoriteResponse(Long favoriteId, Long favoriteCount) {

    public static SpaceRegisterInFavoriteResponse of(Long favoriteId, Long favoriteCount){
        return new SpaceRegisterInFavoriteResponse(favoriteId, favoriteCount);
    }

}
