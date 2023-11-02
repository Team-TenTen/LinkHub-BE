package com.tenten.linkhub.domain.space.facade.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberInfo;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import jakarta.servlet.http.Cookie;

import java.util.List;
import java.util.Map;

public record SpaceDetailGetByIdFacadeResponse(
        Long spaceId,
        String spaceName,
        String description,
        Category category,
        Boolean isVisible,
        Boolean isComment,
        Boolean isLinkSummarizable,
        Boolean isReadMarkEnabled,
        Long viewCount,
        Long scrapCount,
        Long favoriteCount,
        String spaceImagePath,
        List<SpaceMemberDetailInfo> memberDetailInfos,
        Cookie spaceViewCookie
) {
    public static SpaceDetailGetByIdFacadeResponse of(SpaceWithSpaceImageAndSpaceMemberInfo response, MemberInfos memberDetailInfos, Cookie spaceViewCookie){
        Map<Long, MemberInfo> memberInfos = memberDetailInfos.memberInfos();

        List<SpaceMemberDetailInfo> spaceMemberDetailInfos = response.spaceMemberInfos().stream()
                .map(s -> new SpaceMemberDetailInfo(
                        s.memberId(),
                        memberInfos.get(s.memberId()).nickname(),
                        memberInfos.get(s.memberId()).aboutMe(),
                        memberInfos.get(s.memberId()).path(),
                        s.role()
                ))
                .toList();

        return new SpaceDetailGetByIdFacadeResponse(
                response.spaceId(),
                response.spaceName(),
                response.description(),
                response.category(),
                response.isVisible(),
                response.isComment(),
                response.isLinkSummarizable(),
                response.isReadMarkEnabled(),
                response.viewCount(),
                response.scrapCount(),
                response.favoriteCount(),
                response.spaceImagePath(),
                spaceMemberDetailInfos,
                spaceViewCookie
        );
    }

}
