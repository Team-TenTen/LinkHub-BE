package com.tenten.linkhub.domain.space.service.dto.link;

import com.tenten.linkhub.domain.space.repository.link.dto.PopularLinkGetDto;

import java.util.List;
import java.util.Objects;

public record PopularLinksGetByQueryResponses(
        List<PopularLinksGetByQueryResponse> responses
) {
    public static PopularLinksGetByQueryResponses from(List<PopularLinkGetDto> popularLinkGetDtos) {
        List<PopularLinksGetByQueryResponse> responseList = popularLinkGetDtos
                .stream()
                .map(
                        dto -> new PopularLinksGetByQueryResponse(
                                dto.linkId(),
                                dto.title(),
                                dto.url(),
                                dto.tagName(),
                                Objects.isNull(dto.tagColor()) ? null : dto.tagColor().getValue(),
                                dto.likeCount(),
                                dto.isLiked()
                        )
                )
                .toList();

        return new PopularLinksGetByQueryResponses(responseList);
    }
}
