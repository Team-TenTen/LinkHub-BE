package com.tenten.linkhub.domain.space.service.dto.link;

public record PopularLinksGetByQueryResponse(
        Long linkId,
        String title,
        String url,
        String tagName,
        String tagColor,
        Long likeCount,
        boolean isLiked
) {
}
