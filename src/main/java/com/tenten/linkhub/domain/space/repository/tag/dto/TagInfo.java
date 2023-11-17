package com.tenten.linkhub.domain.space.repository.tag.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.space.model.link.Color;

public record TagInfo(
        String name,
        Color color
) {
    @QueryProjection
    public TagInfo(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
