package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceGetDto;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;

public interface SpaceService {
    SpacesFindByQueryResponses findSpacesByQuery(SpacesFindByQueryRequest request);

    Long createSpace(SpaceCreateRequest spaceCreateRequest);

    SpaceWithSpaceImageAndSpaceMemberInfo getSpaceWithSpaceImageAndSpaceMemberById(Long spaceId);

    void checkMemberAddLink(Long memberId, Long spaceId);

    SpaceGetDto getSpace(Long spaceId);
}
