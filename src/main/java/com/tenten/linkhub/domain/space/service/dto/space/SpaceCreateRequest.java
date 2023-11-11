package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.global.aws.dto.ImageInfo;

public record SpaceCreateRequest(
        String spaceName,
        String description,
        Category category,
        Boolean isVisible,
        Boolean isComment,
        Boolean isLinkSummarizable,
        Boolean isReadMarkEnabled,
        Long memberId,
        ImageInfo imageInfo
) {
}
