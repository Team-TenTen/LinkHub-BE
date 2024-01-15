package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.service.dto.space.DeletedSpaceImageNames;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.NewSpacesScrapRequest;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponses;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.dto.spacemember.SpaceMemberRoleChangeRequest;

public interface SpaceService {

    SpacesFindByQueryResponses findPublicSpacesByQuery(PublicSpacesFindByQueryRequest request);

    SpacesFindByQueryResponses findPublicSpacesByLikeQuery(PublicSpacesFindByQueryRequest request);

    Long createSpace(SpaceCreateRequest spaceCreateRequest);

    SpaceWithSpaceImageAndSpaceMemberInfo getSpaceWithSpaceImageAndSpaceMemberById(Long spaceId, Long memberId);

    Long updateSpace(SpaceUpdateRequest spaceUpdateRequest);

    void checkMemberEditLink(Long memberId, Long spaceId);

    DeletedSpaceImageNames deleteSpaceById(Long spaceId, Long memberId);

    SpacesFindByQueryResponses findMemberSpacesByQuery(MemberSpacesFindRequest memberSpacesFindRequest);

    SpaceTagGetResponses getTagsBySpaceId(Long spaceId);

    void checkLinkViewHistory(Long spaceId, Long memberId);

    void checkMemberCanViewLink(Long memberId, Long spaceId);

    Long changeSpaceMembersRole(SpaceMemberRoleChangeRequest request);

    void validateScrapSourceSpace(Long spaceId, Long memberId);

    Long createSpaceAndCopyLinks(NewSpacesScrapRequest request);

    void deleteSpaceMemberByMe(Long spaceId, Long memberId);
}
