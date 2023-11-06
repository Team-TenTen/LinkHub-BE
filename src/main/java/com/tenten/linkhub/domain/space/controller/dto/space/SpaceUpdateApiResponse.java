package com.tenten.linkhub.domain.space.controller.dto.space;

public record SpaceUpdateApiResponse(
        Long spaceId
) {
    public static SpaceUpdateApiResponse from(Long spaceId){
        return new SpaceUpdateApiResponse(spaceId);
    }
}
