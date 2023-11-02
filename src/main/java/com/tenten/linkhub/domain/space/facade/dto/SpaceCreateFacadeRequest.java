package com.tenten.linkhub.domain.space.facade.dto;

import com.tenten.linkhub.domain.space.model.category.Category;
import org.springframework.web.multipart.MultipartFile;

public record SpaceCreateFacadeRequest(
        String spaceName,
        String description,
        Category category,
        Boolean isVisible,
        Boolean isComment,
        Boolean isLinkSummarizable,
        Boolean isReadMarkEnabled,
        Long memberId,
        MultipartFile file
) {
}
