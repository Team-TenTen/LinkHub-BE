package com.tenten.linkhub.domain.space.controller.dto.favorite;

public record SpaceRegisterInFavoriteApiResponse(
        Long favoriteCount
) {
    public static SpaceRegisterInFavoriteApiResponse from(Long favoriteCount){
        return new SpaceRegisterInFavoriteApiResponse(favoriteCount);
    }

}
