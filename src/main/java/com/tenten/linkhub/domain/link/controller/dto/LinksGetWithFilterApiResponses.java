package com.tenten.linkhub.domain.link.controller.dto;

import com.tenten.linkhub.domain.link.service.dto.LinkGetByQueryResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import org.springframework.data.domain.Slice;

import java.util.List;

public record LinksGetWithFilterApiResponses(
        List<LinksGetWithFilterApiResponse> responses,
        PageMetaData pageMetaData
) {
    public static LinksGetWithFilterApiResponses from(LinkGetByQueryResponses responses) {
        Slice<LinksGetWithFilterApiResponse> linkResponses = responses.responses()
                .map(l -> new LinksGetWithFilterApiResponse(
                        l.linkId(),
                        l.title(),
                        l.url(),
                        l.tagName(),
                        l.tagColor(),
                        l.likeCount(),
                        l.isLiked(),
                        l.canLinkSummaraizable(),
                        l.canReadMark(),
                        l.linkViewHistories()
                ));

        PageMetaData pageMetaData = new PageMetaData(
                linkResponses.hasNext(),
                linkResponses.getSize(),
                linkResponses.getNumber());

        return new LinksGetWithFilterApiResponses(
                linkResponses.getContent(),
                pageMetaData
        );
    }
}
