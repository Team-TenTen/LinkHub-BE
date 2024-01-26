package com.tenten.linkhub.domain.link.service.dto;

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
