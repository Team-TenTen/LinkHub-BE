package com.tenten.linkhub.domain.space.controller.dto.space;


import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponse;

import java.util.List;

public record SpaceTagsGetApiResponse(
        List<SpaceTagGetResponse> tags
) {
}
