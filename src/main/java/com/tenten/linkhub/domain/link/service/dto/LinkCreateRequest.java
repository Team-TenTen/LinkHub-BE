package com.tenten.linkhub.domain.link.service.dto;

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
