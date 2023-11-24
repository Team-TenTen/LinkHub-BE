package com.tenten.linkhub.domain.space.controller.dto.space;

public record NewSpacesScrapApiResponse(Long spaceId) {

    public static NewSpacesScrapApiResponse from(Long spaceId) {
        return new NewSpacesScrapApiResponse(spaceId);
    }

}
