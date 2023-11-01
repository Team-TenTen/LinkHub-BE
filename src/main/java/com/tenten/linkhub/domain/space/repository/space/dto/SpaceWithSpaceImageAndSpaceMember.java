package com.tenten.linkhub.domain.space.repository.space.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;

public record SpaceWithSpaceImageAndSpaceMember(Space space, SpaceImage spaceImage, SpaceMember spaceMember) {

    @QueryProjection
    public SpaceWithSpaceImageAndSpaceMember(Space space, SpaceImage spaceImage, SpaceMember spaceMember) {
        this.space = space;
        this.spaceImage = spaceImage;
        this.spaceMember = spaceMember;
    }
}
