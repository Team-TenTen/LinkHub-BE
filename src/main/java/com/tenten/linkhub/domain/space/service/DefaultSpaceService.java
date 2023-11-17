package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.exception.LinkViewHistoryException;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.favorite.FavoriteRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.repository.spacemember.SpaceMemberRepository;
import com.tenten.linkhub.domain.space.repository.tag.TagRepository;
import com.tenten.linkhub.domain.space.repository.tag.dto.TagInfo;
import com.tenten.linkhub.domain.space.service.dto.space.DeletedSpaceImageNames;
import com.tenten.linkhub.domain.space.service.dto.space.MySpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponse;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponses;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.mapper.SpaceMapper;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public PublicSpacesFindByQueryResponses findPublicSpacesByQuery(PublicSpacesFindByQueryRequest request) {
        Slice<Space> spaces = spaceRepository.findPublicSpacesJoinSpaceImageByQuery(mapper.toQueryCond(request));

        return PublicSpacesFindByQueryResponses.from(spaces);
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

        Boolean hasFavorite = favoriteRepository.isExist(memberId, spaceId);

        return SpaceWithSpaceImageAndSpaceMemberInfo.of(space, isOwner, isCanEdit, hasFavorite);
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
    public PublicSpacesFindByQueryResponses findMySpacesByQuery(MySpacesFindRequest request) {
        Slice<Space> spaces = spaceRepository.findMySpacesJoinSpaceImageByQuery(mapper.toMySpacesFindQueryCondition(request));

        return PublicSpacesFindByQueryResponses.from(spaces);
    }

    @Override
    public SpaceTagGetResponses getTagsBySpaceId(Long spaceId) {
        List<TagInfo> tagInfos = tagRepository.findBySpaceIdAndGroupBySpaceName(spaceId);
        List<SpaceTagGetResponse> tagResponses = tagInfos
                .stream()
                .map(t -> new SpaceTagGetResponse(t.name(), t.color().getValue()))
                .toList();

        return SpaceTagGetResponses.from(tagResponses);
    }

    @Override
    public void checkLinkViewHistory(Long memberId, Long spaceId) {
        Space space = spaceRepository.getSpaceJoinSpaceMemberById(spaceId);

        space.checkLinkViewHistoryEnabled(memberId);
    }

}
