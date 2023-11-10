package com.tenten.linkhub.domain.space.facade.dto;

import java.util.List;

public record SpaceDetailGetByIdFacadeRequest(
        Long spaceId,
        Long memberId,
        List<Long> spaceViews
) {
}
