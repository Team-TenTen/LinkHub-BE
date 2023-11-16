package com.tenten.linkhub.domain.space.controller.dto.favorite;

import com.tenten.linkhub.domain.space.service.dto.favorite.SpaceRegisterInFavoriteResponse;

public record SpaceRegisterInFavoriteApiResponse(
        Long favoriteId,
        Long registeredSpaceId
) {
    public static SpaceRegisterInFavoriteApiResponse from(SpaceRegisterInFavoriteResponse response){
        return new SpaceRegisterInFavoriteApiResponse(response.favoriteId(), response.registeredSpaceId());
    }

}
