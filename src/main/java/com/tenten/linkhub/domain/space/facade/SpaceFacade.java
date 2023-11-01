package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.facade.mapper.SpaceFacadeMapper;
import com.tenten.linkhub.domain.space.handler.dto.SpaceIncreaseViewCountDto;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
import jakarta.servlet.http.Cookie;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SpaceFacade {
    private static final String SPACE_IMAGE_FOLDER = "space-image/";
    private static final String SPACE_DEFAULT_IMAGE_PATH = "https://team-10-bucket.s3.ap-northeast-2.amazonaws.com/%08space-image/space-default.png";
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
        ImageInfo imageInfo = getImageInfo(request.file());

        return spaceService.createSpace(
                mapper.toSpaceCreateRequest(request, imageInfo));
    }

    @Transactional(readOnly = true)
    public SpaceDetailGetByIdFacadeResponse getSpaceDetailById(Long spaceId, Cookie spaceViewCookie) {
        SpaceWithSpaceImageAndSpaceMemberInfo response = spaceService.getSpaceWithSpaceImageAndSpaceMemberById(spaceId);

        List<Long> memberIds = getMemberIds(response);
        MemberInfos memberInfos = memberService.findMemberInfosByMemberIds(memberIds);

        spaceViewCookie = increaseSpaceViewCount(spaceViewCookie, response.spaceId());

        return SpaceDetailGetByIdFacadeResponse.of(response, memberInfos, spaceViewCookie);
    }

    private ImageInfo getImageInfo(MultipartFile file) {
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

    private Cookie increaseSpaceViewCount(Cookie spaceViewCookie, Long spaceId) {
        if (spaceViewCookie == null) {
            eventPublisher.publishEvent(
                    new SpaceIncreaseViewCountDto(spaceId)
            );

            Cookie newCookie = new Cookie("spaceView", "[" + spaceId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(COOKIE_EXPIRE_TIME);

            return newCookie;
        }

        if (!spaceViewCookie.getValue().contains("[" + spaceId + "]")) {

            eventPublisher.publishEvent(
                    new SpaceIncreaseViewCountDto(spaceId)
            );

            spaceViewCookie.setValue(spaceViewCookie.getValue() + "_[" + spaceId + "]");
            spaceViewCookie.setPath("/");
            spaceViewCookie.setMaxAge(COOKIE_EXPIRE_TIME);
            return spaceViewCookie;
        }

        return spaceViewCookie;
    }

}
