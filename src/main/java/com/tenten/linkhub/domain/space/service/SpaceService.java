package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceGetByIdResponse;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;

public interface SpaceService {
    SpacesFindByQueryResponses findSpacesByQuery(SpacesFindByQueryRequest request);

    Long createSpace(SpaceCreateRequest spaceCreateRequest);

    SpaceGetByIdResponse getSpaceById(Long spaceId, String cookieValue);
}
