package com.tenten.linkhub.domain.space.repository.common.dto;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;

import java.util.List;

public record SpaceAndSpaceImageOwnerNickName(
        Space space,
        List<SpaceImage> spaceImages,
        String ownerNickName
) {
}
