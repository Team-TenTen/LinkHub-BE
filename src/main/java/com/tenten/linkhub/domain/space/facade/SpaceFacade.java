package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.facade.dto.SpaceUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.mapper.SpaceFacadeMapper;
import com.tenten.linkhub.domain.space.handler.dto.SpaceImagesDeleteDto;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.space.DeletedSpaceImageNames;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
import java.util.List;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpaceFacade {
    private static final String SPACE_IMAGE_FOLDER = "space-image/";
    private static final String SPACE_DEFAULT_IMAGE_PATH = "https://team-10-bucket.s3.ap-northeast-2.amazonaws.com/space-image/space-default.png";
    private static final int COOKIE_EXPIRE_TIME = 60 * 60 * 24;

    private final SpaceService spaceService;
    private final MemberService memberService;
    private final S3Uploader s3Uploader;
    private final SpaceFacadeMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    public SpaceFacade(SpaceService spaceService, MemberService memberService, S3Uploader s3Uploader, SpaceFacadeMapper mapper, ApplicationEventPublisher eventPublisher) {
        this.spaceService = spaceService;
        this.memberService = memberService;
        this.s3Uploader = s3Uploader;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    public Long createSpace(SpaceCreateFacadeRequest request){
        ImageInfo imageInfo = getNewImageInfoOrDefaultImageInfo(request.file());

        return spaceService.createSpace(
                mapper.toSpaceCreateRequest(request, imageInfo));
    }

    @Transactional(readOnly = true)
    public SpaceDetailGetByIdFacadeResponse getSpaceDetailById(SpaceDetailGetByIdFacadeRequest request) {
        SpaceWithSpaceImageAndSpaceMemberInfo response = spaceService.getSpaceWithSpaceImageAndSpaceMemberById(request.spaceId(), request.memberId());

        List<Long> memberIds = getMemberIds(response);
        MemberInfos memberInfos = memberService.findMemberInfosByMemberIds(memberIds);

        return SpaceDetailGetByIdFacadeResponse.of(response, memberInfos);
    }

    @Transactional
    public Long updateSpace(SpaceUpdateFacadeRequest request) {
        Optional<ImageInfo> imageInfo = getNewImageInfoOrEmptyImageInfo(request.file());

        return spaceService.updateSpace(
                mapper.toSpaceUpdateRequest(request, imageInfo));
    }

    @Transactional
    public void deleteSpace(Long spaceId, Long memberId) {
        DeletedSpaceImageNames deletedSpaceImageNames = spaceService.deleteSpaceById(spaceId, memberId);

        eventPublisher.publishEvent(
                new SpaceImagesDeleteDto(deletedSpaceImageNames.fileNames())
        );
    }

    private Optional<ImageInfo> getNewImageInfoOrEmptyImageInfo(MultipartFile file){
        if (file == null){
            return Optional.empty();
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, SPACE_IMAGE_FOLDER);
        return Optional.ofNullable(s3Uploader.saveImage(imageSaveRequest));
    }

    private ImageInfo getNewImageInfoOrDefaultImageInfo(MultipartFile file) {
        if (file == null){
            return ImageInfo.of(SPACE_DEFAULT_IMAGE_PATH, "default-image");
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, SPACE_IMAGE_FOLDER);
        return s3Uploader.saveImage(imageSaveRequest);
    }

    private List<Long> getMemberIds(SpaceWithSpaceImageAndSpaceMemberInfo response) {
        return response.spaceMemberInfos().stream()
                .map(SpaceMemberInfo::memberId)
                .toList();
    }

}
