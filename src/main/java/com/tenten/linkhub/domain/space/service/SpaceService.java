package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.service.dto.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;

public interface SpaceService {
    SpacesFindByQueryResponses findSpacesByQuery(SpacesFindByQueryRequest request);

    Long createSpace(SpaceCreateRequest spaceCreateRequest);
}
