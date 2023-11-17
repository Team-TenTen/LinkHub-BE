package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.service.dto.space.DeletedSpaceImageNames;
import com.tenten.linkhub.domain.space.service.dto.space.MySpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponses;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryResponses;

public interface SpaceService {

    PublicSpacesFindByQueryResponses findPublicSpacesByQuery(PublicSpacesFindByQueryRequest request);

    Long createSpace(SpaceCreateRequest spaceCreateRequest);

    SpaceWithSpaceImageAndSpaceMemberInfo getSpaceWithSpaceImageAndSpaceMemberById(Long spaceId, Long memberId);

    Long updateSpace(SpaceUpdateRequest spaceUpdateRequest);

    void checkMemberEditLink(Long memberId, Long spaceId);

    DeletedSpaceImageNames deleteSpaceById(Long spaceId, Long memberId);

    PublicSpacesFindByQueryResponses findMySpacesByQuery(MySpacesFindRequest mySpacesFindRequest);

    SpaceTagGetResponses getTagsBySpaceId(Long spaceId);
}
