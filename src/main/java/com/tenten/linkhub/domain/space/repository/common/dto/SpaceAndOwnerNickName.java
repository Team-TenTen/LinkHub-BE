package com.tenten.linkhub.domain.space.repository.common.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.space.model.space.Space;

public record SpaceAndOwnerNickName(
        Space space,
        String ownerNickName
) {

    @QueryProjection
    public SpaceAndOwnerNickName(Space space, String ownerNickName) {
        this.space = space;
        this.ownerNickName = ownerNickName;
    }

}
