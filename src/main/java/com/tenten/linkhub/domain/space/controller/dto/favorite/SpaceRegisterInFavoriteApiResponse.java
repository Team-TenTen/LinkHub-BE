package com.tenten.linkhub.domain.space.controller.dto.favorite;

public record SpaceRegisterInFavoriteApiResponse(Long registeredSpaceId) {

    public static SpaceRegisterInFavoriteApiResponse from(Long registeredSpaceId){
        return new SpaceRegisterInFavoriteApiResponse(registeredSpaceId);
    }

}
