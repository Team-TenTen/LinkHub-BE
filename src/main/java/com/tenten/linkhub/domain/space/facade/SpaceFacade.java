package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.handler.dto.SpaceIncreaseViewCountDto;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceMemberInfo;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import jakarta.servlet.http.Cookie;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class SpaceFacade {
    private static final int COOKIE_EXPIRE_TIME = 60 * 60 * 24;

    private final SpaceService spaceService;
    private final MemberService memberService;
    private final ApplicationEventPublisher eventPublisher;

    public SpaceFacade(SpaceService spaceService, MemberService memberService, ApplicationEventPublisher eventPublisher) {
        this.spaceService = spaceService;
        this.memberService = memberService;
        this.eventPublisher = eventPublisher;
    }

    public SpaceDetailGetByIdFacadeResponse getSpaceDetailById(Long spaceId, Cookie spaceViewCookie) {
        SpaceWithSpaceImageAndSpaceMemberInfo response = spaceService.getSpaceWithSpaceImageAndSpaceMemberById(spaceId);

        List<Long> memberIds = getMemberIds(response);
        MemberInfos memberInfos = memberService.findMemberInfosByMemberIds(memberIds);

        spaceViewCookie = increaseSpaceViewCount(spaceViewCookie, response.spaceId());

        return SpaceDetailGetByIdFacadeResponse.of(response, memberInfos, spaceViewCookie);
    }

    private List<Long> getMemberIds(SpaceWithSpaceImageAndSpaceMemberInfo response) {
        return response.spaceMemberInfos().stream()
                .map(SpaceMemberInfo::memberId)
                .toList();
    }

    private Cookie increaseSpaceViewCount(Cookie spaceViewCookie, Long spaceId) {
        if (spaceViewCookie != null){
            if(!spaceViewCookie.getValue().contains("[" + spaceId + "]")) {

                eventPublisher.publishEvent(
                        new SpaceIncreaseViewCountDto(spaceId)
                );

                spaceViewCookie.setValue(spaceViewCookie.getValue() + "_[" + spaceId + "]" ); //기존 쿠기내에 해당 게시물 Id 추가
                spaceViewCookie.setPath("/");
                spaceViewCookie.setMaxAge(COOKIE_EXPIRE_TIME);
            }
        }

        return spaceViewCookie;
    }

}
