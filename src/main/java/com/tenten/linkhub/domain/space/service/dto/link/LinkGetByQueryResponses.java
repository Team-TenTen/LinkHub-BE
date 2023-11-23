package com.tenten.linkhub.domain.space.service.dto.link;

import com.tenten.linkhub.domain.space.repository.link.dto.LinkGetDto;
import org.springframework.data.domain.Slice;
import java.util.Objects;


public record LinkGetByQueryResponses(
        Slice<LinkGetByQueryResponse> responses
) {
    public static LinkGetByQueryResponses from(Slice<LinkGetDto> linkGetDtos) {
        Slice<LinkGetByQueryResponse> responseList = linkGetDtos
                .map(dto -> new LinkGetByQueryResponse(
                        dto.getLinkId(),
                        dto.getTitle(),
                        dto.getUrl(),
                        dto.getTagName(),
                        Objects.isNull(dto.getTagColor()) ? null : dto.getTagColor().getValue(),
                        dto.getLikeCount(),
                        dto.isLiked(),
                        dto.isCanLinkSummaraizable(),
                        dto.isCanReadMark(),
                        dto.getLinkViewHistories()
                ));

        return new LinkGetByQueryResponses(responseList);
    }
}
