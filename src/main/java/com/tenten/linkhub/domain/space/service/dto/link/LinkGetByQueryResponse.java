package com.tenten.linkhub.domain.space.service.dto.link;

import com.tenten.linkhub.domain.space.repository.link.dto.LinkViewDto;

import java.util.List;

public record LinkGetByQueryResponse(
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
