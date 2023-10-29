package com.tenten.linkhub.domain.space.controller.dto.space;

public record SpaceCreateApiResponse(Long spaceId) {

    public static SpaceCreateApiResponse from(Long spaceId){
        return new SpaceCreateApiResponse(spaceId);
    }

}
