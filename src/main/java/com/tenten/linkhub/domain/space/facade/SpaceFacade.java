package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.facade.dto.NewSpacesScrapFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.facade.dto.SpaceUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.mapper.SpaceFacadeMapper;
import com.tenten.linkhub.domain.space.handler.dto.SpaceImagesDeleteEvent;
import com.tenten.linkhub.domain.space.handler.dto.SpaceIncreaseViewCountEvent;
import com.tenten.linkhub.domain.space.service.LinkService;
import com.tenten.linkhub.domain.space.service.SpaceImageUploader;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.space.DeletedSpaceImageNames;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import com.tenten.linkhub.global.aws.dto.ImageInfo;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SpaceFacade {

    private final SpaceService spaceService;
    private final MemberService memberService;
    private final SpaceImageUploader spaceImageUploader;
    private final LinkService linkService;
    private final SpaceFacadeMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    public Long createSpace(SpaceCreateFacadeRequest request) {
        ImageInfo imageInfo = spaceImageUploader.getNewImageInfoOrDefaultImageInfo(request.file());

        return spaceService.createSpace(
                mapper.toSpaceCreateRequest(request, imageInfo));
    }

    @Transactional(readOnly = true)
    public SpaceDetailGetByIdFacadeResponse getSpaceDetailById(SpaceDetailGetByIdFacadeRequest request) {
        SpaceWithSpaceImageAndSpaceMemberInfo response = spaceService.getSpaceWithSpaceImageAndSpaceMemberById(request.spaceId(), request.memberId());

        List<Long> memberIds = getMemberIds(response);
        MemberInfos memberInfos = memberService.findMemberInfosByMemberIds(memberIds);

        List<Long> newSpaceViews = increaseSpaceViewCountAndSetSpaceViews(request.spaceViews(), response.spaceId());

        return SpaceDetailGetByIdFacadeResponse.of(response, memberInfos, newSpaceViews);
    }

    @Transactional
    public Long updateSpace(SpaceUpdateFacadeRequest request) {
        Optional<ImageInfo> imageInfo = spaceImageUploader.getNewImageInfoOrEmptyImageInfo(request.file());

        return spaceService.updateSpace(
                mapper.toSpaceUpdateRequest(request, imageInfo));
    }

    @Transactional
    public void deleteSpace(Long spaceId, Long memberId) {
        DeletedSpaceImageNames deletedSpaceImageNames = spaceService.deleteSpaceById(spaceId, memberId);

        eventPublisher.publishEvent(
                new SpaceImagesDeleteEvent(deletedSpaceImageNames.fileNames())
        );
    }

    public Long scrapAndCreateNewSpace(NewSpacesScrapFacadeRequest request) {
        spaceService.validateScrapTargetSpace(request.targetSpaceId(), request.memberId());

        createSpace(mapper.toSpaceCreateFacadeRequest(request));
        linkService.
    }

    private List<Long> getMemberIds(SpaceWithSpaceImageAndSpaceMemberInfo response) {
        return response.spaceMemberInfos().stream()
                .map(SpaceMemberInfo::memberId)
                .toList();
    }

    private List<Long> increaseSpaceViewCountAndSetSpaceViews(List<Long> spaceViews, Long spaceId) {
        if (spaceViews.isEmpty()) {
            eventPublisher.publishEvent(
                    new SpaceIncreaseViewCountEvent(spaceId)
            );

            spaceViews.add(spaceId);
            return spaceViews;
        }

        if (!spaceViews.contains(spaceId)) {
            eventPublisher.publishEvent(
                    new SpaceIncreaseViewCountEvent(spaceId)
            );

            spaceViews.add(spaceId);
            return spaceViews;
        }

        return spaceViews;
    }

}
