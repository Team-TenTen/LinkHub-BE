package com.tenten.linkhub.domain.link.repository.link.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.link.model.Color;

import java.time.LocalDateTime;


public record LinkInfoDto(
        Long linkId,
        String title,
        String url,
        String tagName,
        Color tagColor,
        Long likeCount,
        boolean isLiked,
        boolean canLinkSummaraizable,
        boolean canReadMark,
        LocalDateTime createdAt
) {
    @QueryProjection
    public LinkInfoDto(Long linkId,
                       String title,
                       String url,
                       String tagName,
                       Color tagColor,
                       Long likeCount,
                       boolean isLiked,
                       boolean canLinkSummaraizable,
                       boolean canReadMark,
                       LocalDateTime createdAt) {
        this.linkId = linkId;
        this.title = title;
        this.url = url;
        this.tagName = tagName;
        this.tagColor = tagColor;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.canLinkSummaraizable = canLinkSummaraizable;
        this.canReadMark = canReadMark;
        this.createdAt = createdAt;
    }
}
