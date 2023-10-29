package com.tenten.linkhub.domain.space.repository.space.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;

public record SpaceWithSpaceImage(Space space, SpaceImage spaceImage) {

    @QueryProjection
    public SpaceWithSpaceImage(Space space, SpaceImage spaceImage) {
        this.space = space;
        this.spaceImage = spaceImage;
    }

}
