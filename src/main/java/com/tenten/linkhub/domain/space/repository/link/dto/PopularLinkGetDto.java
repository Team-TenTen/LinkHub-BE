package com.tenten.linkhub.domain.space.repository.link.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tenten.linkhub.domain.space.model.link.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopularLinkGetDto {
    private Long linkId;
    private String title;
    private String url;
    private String tagName;
    private Color tagColor;
    private Long likeCount;
    private boolean isLiked;

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
