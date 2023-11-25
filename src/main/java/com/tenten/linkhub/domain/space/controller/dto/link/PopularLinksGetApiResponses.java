package com.tenten.linkhub.domain.space.controller.dto.link;

import com.tenten.linkhub.domain.space.service.dto.link.PopularLinksGetByQueryResponses;

import java.util.List;

public record PopularLinksGetApiResponses(
        List<PopularLinksGetApiResponse> responses
) {
    public static PopularLinksGetApiResponses from(PopularLinksGetByQueryResponses responses) {
        List<PopularLinksGetApiResponse> linkResponses = responses.responses()
                .stream()
                .map(l -> new PopularLinksGetApiResponse(
                        l.linkId(),
                        l.title(),
                        l.url(),
                        l.tagName(),
                        l.tagColor(),
                        l.likeCount(),
                        l.isLiked()
                ))
                .toList();

        return new PopularLinksGetApiResponses(linkResponses);
    }
}
