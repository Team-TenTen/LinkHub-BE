package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.mapper.SpaceMapper;

import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;
import com.tenten.linkhub.global.aws.s3.S3Uploader;

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
    private final S3Uploader s3Uploader;
    private final SpaceMapper mapper;

    public DefaultSpaceService(SpaceRepository spaceRepository, S3Uploader s3Uploader, SpaceMapper mapper) {
        this.spaceRepository = spaceRepository;
        this.s3Uploader = s3Uploader;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public SpacesFindByQueryResponses findSpacesByQuery(SpacesFindByQueryRequest request) {
        Slice<Space> spaces = spaceRepository.findSpaceWithSpaceImageByQuery(mapper.toQueryCond(request));

        return SpacesFindByQueryResponses.from(spaces);
    }

    @Override
    @Transactional
    public Long createSpace(SpaceCreateRequest request) {
        Space space = mapper.toSpace(request);

        space.addSpaceMember(
                mapper.toSpaceMember(request, OWNER)
        );

        ImageInfo imageInfo = getImageInfo(request.file());

        space.addSpaceImage(
                mapper.toSpaceImage(imageInfo)
        );

        return spaceRepository.save(space).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public SpaceWithSpaceImageAndSpaceMemberInfo getSpaceWithSpaceImageAndSpaceMemberById(Long spaceId) {
        Space space = spaceRepository.getSpaceJoinSpaceImageAndSpaceMemberById(spaceId);

        return SpaceWithSpaceImageAndSpaceMemberInfo.from(space);
    }

    private ImageInfo getImageInfo(MultipartFile file) {
        if (file == null){
            return ImageInfo.of(SPACE_DEFAULT_IMAGE_PATH, "default-image");
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, SPACE_IMAGE_FOLDER);
        return s3Uploader.saveImage(imageSaveRequest);
    }

}
