package com.tenten.linkhub.domain.link.repository.link.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.link.model.Color;


public record PopularLinkGetDto(
        Long linkId,
        String title,
        String url,
        String tagName,
        Color tagColor,
        Long likeCount,
        boolean isLiked
) {
    @QueryProjection
    public PopularLinkGetDto(Long linkId, String title, String url, String tagName, Color tagColor, Long likeCount, boolean isLiked) {
        this.linkId = linkId;
        this.title = title;
        this.url = url;
        this.tagName = tagName;
        this.tagColor = tagColor;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }
}
