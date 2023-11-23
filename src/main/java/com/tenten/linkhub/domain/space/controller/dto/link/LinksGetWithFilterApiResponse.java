package com.tenten.linkhub.domain.space.controller.dto.link;

import com.tenten.linkhub.domain.space.repository.link.dto.LinkViewDto;

import java.util.List;

public record LinksGetWithFilterApiResponse(
        Long linkId,
        String title,
        String url,
        String tagName,
        String tagColor,
        Long likeCount,
        boolean isLiked,
        boolean canLinkSummaraizable,
        boolean canReadMark,
        List<LinkViewDto> linkViewHistories
) {
}
