package com.tenten.linkhub.domain.space.facade.dto;

public record SpaceDetailGetByIdFacadeRequest(
        Long spaceId,
        Long memberId
) {
}
