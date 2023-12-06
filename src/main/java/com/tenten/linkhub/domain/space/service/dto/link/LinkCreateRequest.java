package com.tenten.linkhub.domain.space.service.dto.link;

import java.util.Objects;

public record LinkCreateRequest(
        Long spaceId,
        String url,
        String title,
        String tagName,
        Long memberId,
        String color
) {
    public boolean hasCreateTagInfo() {
        return Objects.nonNull(tagName) && Objects.nonNull(color);
    }
}
