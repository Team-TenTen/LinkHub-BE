package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceWithSpaceImage;
import com.tenten.linkhub.domain.space.repository.spaceimage.SpaceImageRepository;
import com.tenten.linkhub.domain.space.repository.spaceimage.dto.SpaceMemberWithMemberInfo;
import com.tenten.linkhub.domain.space.repository.spacemember.SpaceMemberRepository;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceGetByIdResponse;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.mapper.SpaceMapper;

import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;
import com.tenten.linkhub.global.aws.s3.S3Uploader;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.tenten.linkhub.domain.space.model.space.Role.OWNER;

@Service
public class DefaultSpaceService implements SpaceService{
    private static final String SPACE_IMAGE_FOLDER = "space-image/";
    private static final String SPACE_DEFAULT_IMAGE_PATH = "https://team-10-bucket.s3.ap-northeast-2.amazonaws.com/%08space-image/space-default.png";

    private final SpaceRepository spaceRepository;
    private final SpaceMemberRepository spaceMemberRepository;
    private final SpaceImageRepository spaceImageRepository;
    private final S3Uploader s3Uploader;
    private final ApplicationEventPublisher eventPublisher;
    private final SpaceMapper mapper;

    public DefaultSpaceService(SpaceRepository spaceRepository, SpaceMemberRepository spaceMemberRepository, SpaceImageRepository spaceImageRepository, S3Uploader s3Uploader, ApplicationEventPublisher eventPublisher, SpaceMapper mapper) {
        this.spaceRepository = spaceRepository;
        this.spaceMemberRepository = spaceMemberRepository;
        this.spaceImageRepository = spaceImageRepository;
        this.s3Uploader = s3Uploader;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public SpacesFindByQueryResponses findSpacesByQuery(SpacesFindByQueryRequest request) {
        Slice<SpaceWithSpaceImage> spaces = spaceRepository.findSpaceWithSpaceImageByQuery(mapper.toQueryCond(request));

        return SpacesFindByQueryResponses.from(spaces);
    }

    @Override
    @Transactional
    public Long createSpace(SpaceCreateRequest request) {
        Space savedSpace = spaceRepository.save(mapper.toSpace(request));

        spaceMemberRepository.save(
                mapper.toSpaceMember(savedSpace, request, OWNER)
        );

        ImageInfo imageInfo = getImageInfo(request.file());

        spaceImageRepository.save(
                mapper.toSpaceImage(savedSpace, imageInfo)
        );

        return savedSpace.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public SpaceGetByIdResponse getSpaceById(Long spaceId, String cookieValue) {
        SpaceWithSpaceImage spaceWithSpaceImage = spaceRepository.getSpaceWithSpaceImageById(spaceId);

        SpaceMemberWithMemberInfo response = spaceMemberRepository.findSpaceMemberWithMemberInfoBySpaceId(spaceId);

    }

    private ImageInfo getImageInfo(MultipartFile file) {
        if (file == null){
            return ImageInfo.of(SPACE_DEFAULT_IMAGE_PATH, "default-image");
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, SPACE_IMAGE_FOLDER);
        return s3Uploader.saveImage(imageSaveRequest);
    }

}
