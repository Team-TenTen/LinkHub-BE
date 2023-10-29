package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceWithSpaceImage;
import com.tenten.linkhub.domain.space.repository.spaceimage.SpaceImageRepository;
import com.tenten.linkhub.domain.space.repository.spacemember.SpaceMemberRepository;
import com.tenten.linkhub.domain.space.service.dto.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.mapper.SpaceMapper;

import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;
import com.tenten.linkhub.global.aws.s3.S3Uploader;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tenten.linkhub.domain.space.model.space.Role.OWNER;

@Service
public class DefaultSpaceService implements SpaceService{
    private static final String SPACE_IMAGE_FOLDER = "space-image/";

    private final SpaceRepository spaceRepository;
    private final SpaceMemberRepository spaceMemberRepository;
    private final SpaceImageRepository spaceImageRepository;
    private final S3Uploader s3Uploader;
    private final SpaceMapper mapper;

    public DefaultSpaceService(SpaceRepository spaceRepository, SpaceMemberRepository spaceMemberRepository, SpaceImageRepository spaceImageRepository, S3Uploader s3Uploader, SpaceMapper mapper) {
        this.spaceRepository = spaceRepository;
        this.spaceMemberRepository = spaceMemberRepository;
        this.spaceImageRepository = spaceImageRepository;
        this.s3Uploader = s3Uploader;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public SpacesFindByQueryResponses findSpacesByQuery(SpacesFindByQueryRequest request) {
        Slice<SpaceWithSpaceImage> spaces = spaceRepository.findByQuery(mapper.toQueryCond(request));

        return SpacesFindByQueryResponses.from(spaces);
    }

    @Override
    @Transactional
    public Long createSpace(SpaceCreateRequest request) {
        Space savedSpace = spaceRepository.save(mapper.toSpace(request));

        spaceMemberRepository.save(
                mapper.toSpaceMember(savedSpace, request, OWNER)
        );

        ImageInfo imageInfo = s3Uploader.saveImage(
                ImageSaveRequest.of(request.file(), SPACE_IMAGE_FOLDER)
        );

        spaceImageRepository.save(
                mapper.toSpaceImage(savedSpace, imageInfo)
        );

        return savedSpace.getId();
    }

}
