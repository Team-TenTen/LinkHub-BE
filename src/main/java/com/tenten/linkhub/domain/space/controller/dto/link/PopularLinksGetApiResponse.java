package com.tenten.linkhub.domain.space.controller.dto.link;

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
