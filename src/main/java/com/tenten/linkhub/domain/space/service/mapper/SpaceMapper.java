package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.model.space.dto.SpaceUpdateDto;
import com.tenten.linkhub.domain.space.repository.space.dto.CursorPageQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.MemberSpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.NewSpacesScrapRequest;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindWithFilterRequest;
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

    public CursorPageQueryCondition toCursorPageQueryCondition(PublicSpacesFindWithFilterRequest request) {
        return new CursorPageQueryCondition(
                request.pageable(),
                request.lastFavoriteCount(),
                request.lastSpaceId()
        );
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

    public Space toSpace(NewSpacesScrapRequest request, SpaceMember spaceMember, SpaceImage spaceImage) {
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

    public SpaceMember toSpaceMember(Long memberId, Role role) {
        return new SpaceMember(
                memberId,
                role);
    }

    public SpaceImage toSpaceImage(ImageInfo imageInfo) {
        return new SpaceImage(
                imageInfo.path(),
                imageInfo.name());
    }

    public MemberSpacesQueryCondition toMemberSpacesQueryCondition(MemberSpacesFindRequest request, Boolean isMySpace) {
        return new MemberSpacesQueryCondition(
                request.pageable(),
                request.keyWord(),
                request.filter(),
                request.targetMemberId(),
                isMySpace);
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
