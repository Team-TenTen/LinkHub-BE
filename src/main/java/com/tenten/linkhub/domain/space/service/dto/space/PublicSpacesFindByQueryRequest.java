package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import org.springframework.data.domain.Pageable;

public record PublicSpacesFindByQueryRequest(
        Pageable pageable,
        String keyWord,
        Category filter
) {
}
