package com.tenten.linkhub.domain.link.service.dto;

import com.tenten.linkhub.domain.link.repository.link.dto.LinkGetDto;
import org.springframework.data.domain.Slice;

import java.util.Objects;


public record LinkGetByQueryResponses(
        Slice<LinkGetByQueryResponse> responses
) {
    public static LinkGetByQueryResponses from(Slice<LinkGetDto> linkGetDtos) {
        Slice<LinkGetByQueryResponse> responseList = linkGetDtos
                .map(dto -> new LinkGetByQueryResponse(
                        dto.linkInfoDto().linkId(),
                        dto.linkInfoDto().title(),
                        dto.linkInfoDto().url(),
                        dto.linkInfoDto().tagName(),
                        Objects.isNull(dto.linkInfoDto().tagColor()) ? null : dto.linkInfoDto().tagColor().getValue(),
                        dto.linkInfoDto().likeCount(),
                        dto.linkInfoDto().isLiked(),
                        dto.linkInfoDto().canLinkSummaraizable(),
                        dto.linkInfoDto().canReadMark(),
                        dto.linkViewHistories()
                ));

        return new LinkGetByQueryResponses(responseList);
    }
}
