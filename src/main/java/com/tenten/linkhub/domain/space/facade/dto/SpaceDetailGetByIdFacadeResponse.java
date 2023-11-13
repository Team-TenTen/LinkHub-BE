package com.tenten.linkhub.domain.space.facade.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberInfo;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceWithSpaceImageAndSpaceMemberInfo;
import jakarta.servlet.http.Cookie;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Boolean isOwner,
        Boolean isCanEdit,
        List<Long> spaceViews
) {
    public static SpaceDetailGetByIdFacadeResponse of(
            SpaceWithSpaceImageAndSpaceMemberInfo response,
            MemberInfos memberDetailInfos,
            List<Long> spaceViews
    ){
        Map<Long, MemberInfo> memberInfos = memberDetailInfos.memberInfos();

        List<SpaceMemberDetailInfo> spaceMemberDetailInfos = response.spaceMemberInfos().stream()
                .map(s -> {
                    MemberInfo memberInfo = memberInfos.get(s.memberId());

                    return new SpaceMemberDetailInfo(
                            s.memberId(),
                            Objects.isNull(memberInfo) ? null : memberInfo.nickname(),
                            Objects.isNull(memberInfo) ? null : memberInfo.aboutMe(),
                            Objects.isNull(memberInfo) ? null : memberInfo.path(),
                            s.role()
                    );
                })
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
                response.isOwner(),
                response.isCanEdit(),
                spaceViews
        );
    }

}
