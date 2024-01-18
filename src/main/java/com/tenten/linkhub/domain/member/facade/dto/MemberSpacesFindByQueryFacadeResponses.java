package com.tenten.linkhub.domain.member.facade.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberNicknames;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import org.springframework.data.domain.Slice;

public record MemberSpacesFindByQueryFacadeResponses(
        Slice<MemberSpacesFindByQueryFacadeResponse> responses
) {

    public static MemberSpacesFindByQueryFacadeResponses of(SpacesFindByQueryResponses spacesFindByQueryResponses, MemberNicknames memberNicknames) {
        Slice<MemberSpacesFindByQueryFacadeResponse> mapResponses = spacesFindByQueryResponses.responses()
                .map(r -> new MemberSpacesFindByQueryFacadeResponse(
                        r.spaceId(),
                        r.spaceName(),
                        r.description(),
                        r.category(),
                        r.isVisible(),
                        r.isComment(),
                        r.isLinkSummarizable(),
                        r.isReadMarkEnabled(),
                        r.viewCount(),
                        r.scrapCount(),
                        r.favoriteCount(),
                        r.spaceImagePath(),
                        memberNicknames.memberNicknames().get(r.ownerId())
                ));

        return new MemberSpacesFindByQueryFacadeResponses(mapResponses);
    }

    public record MemberSpacesFindByQueryFacadeResponse(
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
            String ownerNickName
    ) { }
}
