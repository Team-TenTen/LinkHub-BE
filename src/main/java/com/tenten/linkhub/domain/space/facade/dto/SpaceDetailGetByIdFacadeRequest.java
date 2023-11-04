package com.tenten.linkhub.domain.space.facade.dto;

import jakarta.servlet.http.Cookie;

public record SpaceDetailGetByIdFacadeRequest(
        Long spaceId,
        Cookie spaceViewCookie,
        Long memberId
) {
}
