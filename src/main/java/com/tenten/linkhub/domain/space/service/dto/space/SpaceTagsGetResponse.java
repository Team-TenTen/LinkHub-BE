package com.tenten.linkhub.domain.space.service.dto.space;

import java.util.List;

public record SpaceTagsGetResponse(
        List<String> tagNames
) {
    public static SpaceTagsGetResponse from(List<String> tagNames) {
        return new SpaceTagsGetResponse(tagNames);
    }
}
