package com.tenten.linkhub.domain.space.repository.link.dto;

import com.tenten.linkhub.domain.space.model.link.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LinkGetDto {
    private Long linkId;
    private String title;
    private String url;
    private String tagName;
    private Color tagColor;
    private Long likeCount;
    private boolean isLiked;
    private boolean canLinkSummaraizable;
    private boolean canReadMark;
    private List<LinkViewDto> linkViewHistories;
}
