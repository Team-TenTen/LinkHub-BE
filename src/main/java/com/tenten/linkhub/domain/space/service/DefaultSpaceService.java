package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickName;
import com.tenten.linkhub.domain.space.repository.favorite.FavoriteRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.repository.space.dto.MemberSpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.spacemember.SpaceMemberRepository;
import com.tenten.linkhub.domain.space.repository.tag.TagRepository;
import com.tenten.linkhub.domain.space.repository.tag.dto.TagInfo;
import com.tenten.linkhub.domain.space.service.dto.space.DeletedSpaceImageNames;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponse;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponses;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.dto.spacemember.SpaceMemberRoleChangeRequest;
import com.tenten.linkhub.domain.space.service.mapper.SpaceMapper;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.tenten.linkhub.domain.space.model.space.Role.OWNER;

@RequiredArgsConstructor
@Service
public class DefaultSpaceService implements SpaceService {

    private final SpaceRepository spaceRepository;
    private final SpaceMemberRepository spaceMemberRepository;
    private final FavoriteRepository favoriteRepository;
    private final TagRepository tagRepository;
    private final SpaceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public SpacesFindByQueryResponses findPublicSpacesByQuery(PublicSpacesFindByQueryRequest request) {
        Slice<SpaceAndSpaceImageOwnerNickName> spaceAndSpaceImageOwnerNickName = spaceRepository.findPublicSpacesJoinSpaceImageByQuery(mapper.toQueryCond(request));

        return SpacesFindByQueryResponses.from(spaceAndSpaceImageOwnerNickName);
    }

    @Override
    @Transactional
    public Long createSpace(SpaceCreateRequest request) {
        SpaceMember spaceMember = mapper.toSpaceMember(request, OWNER);
        SpaceImage spaceImage = mapper.toSpaceImage(request.imageInfo());

        Space space = mapper.toSpace(request, spaceMember, spaceImage);

        return spaceRepository.save(space).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public SpaceWithSpaceImageAndSpaceMemberInfo getSpaceWithSpaceImageAndSpaceMemberById(Long spaceId, Long memberId) {
        Space space = spaceRepository.getSpaceJoinSpaceMemberById(spaceId);

        space.validateVisibilityAndMembership(memberId);

        Boolean isOwner = space.isOwner(memberId);
        Boolean isCanEdit = space.isCanEdit(memberId);
        List<SpaceMember> sortedSpaceMember = space.getSortedSpaceMember();

        Boolean hasFavorite = favoriteRepository.isExist(memberId, spaceId);

        return SpaceWithSpaceImageAndSpaceMemberInfo.of(space, sortedSpaceMember, isOwner, isCanEdit, hasFavorite);
    }

    @Override
    @Transactional
    public Long updateSpace(SpaceUpdateRequest request) {
        Space space = spaceRepository.getById(request.spaceId());
        space.updateSpaceAttributes(mapper.toSpaceUpdateDto(request));

        return space.getId();
    }

    @Override
    public void checkMemberEditLink(Long memberId, Long spaceId) {
        if (!spaceMemberRepository.existsAuthorizedSpaceMember(memberId, spaceId)) {
            throw new UnauthorizedAccessException("링크를 생성할 수 있는 권한이 없습니다.");
        }
    }

    @Override
    @Transactional
    public DeletedSpaceImageNames deleteSpaceById(Long spaceId, Long memberId) {
        Space space = spaceRepository.getById(spaceId);
        space.deleteSpace(memberId);

        return DeletedSpaceImageNames.from(space.getAllSpaceImages());
    }

    @Override
    @Transactional(readOnly = true)
    public SpacesFindByQueryResponses findMemberSpacesByQuery(MemberSpacesFindRequest request) {
        Boolean isMySpace = Objects.equals(request.requestMemberId(), request.targetMemberId());
        MemberSpacesQueryCondition queryCondition = mapper.toMemberSpacesQueryCondition(request, isMySpace);

        Slice<SpaceAndSpaceImageOwnerNickName> spaceAndSpaceImageOwnerNickName = spaceRepository.findMemberSpacesJoinSpaceImageByQuery(queryCondition);

        return SpacesFindByQueryResponses.from(spaceAndSpaceImageOwnerNickName);
    }

    @Override
    public SpaceTagGetResponses getTagsBySpaceId(Long spaceId) {
        List<TagInfo> tagInfos = tagRepository.findBySpaceIdAndGroupBySpaceName(spaceId);
        List<SpaceTagGetResponse> tagResponses = tagInfos
                .stream()
                .map(t -> new SpaceTagGetResponse(t.name(), t.color().getValue(), t.tagId()))
                .toList();

        return SpaceTagGetResponses.from(tagResponses);
    }

    @Override
    public void checkLinkViewHistory(Long spaceId, Long memberId) {
        Space space = spaceRepository.getSpaceJoinSpaceMemberById(spaceId);

        space.checkLinkViewHistoryEnabled(memberId);
    }

    @Override
    public void checkMemberCanViewLink(Long memberId, Long spaceId) {
        Space space = spaceRepository.getSpaceJoinSpaceMemberById(spaceId);
        space.validateVisibilityAndMembership(memberId);
    }


    @Override
    @Transactional
    public Long changeSpaceMembersRole(SpaceMemberRoleChangeRequest request) {
        Space space = spaceRepository.getById(request.spaceId());
        space.validateOwnership(request.myMemberId());

        space.changeSpaceMembersRole(request.targetMemberId(), request.role());

        return space.getId();
    }

}
