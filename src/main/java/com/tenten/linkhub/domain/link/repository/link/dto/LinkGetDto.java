package com.tenten.linkhub.domain.link.repository.link.dto;

import java.util.List;

public record LinkGetDto(
        LinkInfoDto linkInfoDto,
        List<LinkViewDto> linkViewHistories
) {
}
