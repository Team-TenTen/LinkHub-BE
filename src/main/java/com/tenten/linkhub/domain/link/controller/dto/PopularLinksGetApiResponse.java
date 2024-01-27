package com.tenten.linkhub.domain.link.controller.dto;

public record PopularLinksGetApiResponse(
        Long linkId,
        String title,
        String url,
        String tagName,
        String tagColor,
        Long likeCount,
        boolean isLiked
) {
}
