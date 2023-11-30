package com.tenten.linkhub.domain.space.repository.link.dto;

import java.util.List;

public record LinkGetDto(
        LinkInfoDto linkInfoDto,
        List<LinkViewDto> linkViewHistories
) {
}
