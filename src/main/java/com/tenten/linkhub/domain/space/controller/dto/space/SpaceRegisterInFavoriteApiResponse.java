package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.service.dto.space.SpaceRegisterInFavoriteResponse;

public record SpaceRegisterInFavoriteApiResponse(
        Long favoriteId,
        Long registeredSpaceId
) {
    public static SpaceRegisterInFavoriteApiResponse from(SpaceRegisterInFavoriteResponse response){
        return new SpaceRegisterInFavoriteApiResponse(response.favoriteId(), response.registeredSpaceId());
    }

}
