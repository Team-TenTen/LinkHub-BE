package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.model.space.dto.SpaceUpdateDto;
import com.tenten.linkhub.domain.space.repository.space.dto.MemberSpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryRequest;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpaceMapper {

    public QueryCondition toQueryCond(PublicSpacesFindByQueryRequest request) {
        return new QueryCondition(
                request.pageable(),
                request.keyWord(),
                request.filter());
    }

    public Space toSpace(SpaceCreateRequest request, SpaceMember spaceMember, SpaceImage spaceImage) {
        return new Space(
                request.memberId(),
                request.spaceName(),
                request.description(),
                request.category(),
                spaceImage,
                spaceMember,
                request.isVisible(),
                request.isComment(),
                request.isLinkSummarizable(),
                request.isReadMarkEnabled());
    }

    public SpaceMember toSpaceMember(SpaceCreateRequest request, Role role) {
        return new SpaceMember(
                request.memberId(),
                role);
    }

    public SpaceImage toSpaceImage(ImageInfo imageInfo) {
        return new SpaceImage(
                imageInfo.path(),
                imageInfo.name());
    }

    public MemberSpacesQueryCondition toMemberSpacesQueryCondition(MemberSpacesFindRequest request, Boolean isSelfSpace) {
        return new MemberSpacesQueryCondition(
                request.pageable(),
                request.keyWord(),
                request.filter(),
                request.targetMemberId(),
                isSelfSpace);
    }

    public SpaceUpdateDto toSpaceUpdateDto(SpaceUpdateRequest request) {
        Optional<ImageInfo> imageInfo = request.imageInfo();
        Optional<SpaceImage> spaceImage = imageInfo.map(i -> new SpaceImage(i.path(), i.name()));

        return new SpaceUpdateDto(
                request.memberId(),
                request.spaceName(),
                request.description(),
                request.category(),
                request.isVisible(),
                request.isComment(),
                request.isLinkSummarizable(),
                request.isReadMarkEnabled(),
                spaceImage
        );
    }
}
