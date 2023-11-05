package com.tenten.linkhub.domain.space.model.space.dto;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.global.aws.dto.ImageInfo;

import java.util.Optional;

public record SpaceUpdateDto(
        Long memberId,
        String spaceName,
        String description,
        Category category,
        Boolean isVisible,
        Boolean isComment,
        Boolean isLinkSummarizable,
        Boolean isReadMarkEnabled,
        Optional<SpaceImage> spaceImage
) {
}
