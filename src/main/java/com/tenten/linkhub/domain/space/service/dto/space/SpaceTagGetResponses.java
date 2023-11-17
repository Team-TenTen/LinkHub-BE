package com.tenten.linkhub.domain.space.service.dto.space;

import java.util.List;

public record SpaceTagGetResponses(
        List<SpaceTagGetResponse> tags
) {
    public static SpaceTagGetResponses from(List<SpaceTagGetResponse> list) {
        return new SpaceTagGetResponses(list);
    }
}
