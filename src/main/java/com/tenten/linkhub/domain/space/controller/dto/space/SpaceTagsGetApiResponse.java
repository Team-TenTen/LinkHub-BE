package com.tenten.linkhub.domain.space.controller.dto.space;

import java.util.List;

public record SpaceTagsGetApiResponse(
        List<String> tagNames
) {
}
