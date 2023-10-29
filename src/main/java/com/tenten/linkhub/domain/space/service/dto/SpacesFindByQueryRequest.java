package com.tenten.linkhub.domain.space.service.dto;

import com.tenten.linkhub.domain.space.model.category.Category;
import org.springframework.data.domain.Pageable;

public record SpacesFindByQueryRequest(
        Pageable pageable,
        String keyWord,
        Category filter
) {
}
