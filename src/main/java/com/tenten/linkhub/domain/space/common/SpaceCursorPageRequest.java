package com.tenten.linkhub.domain.space.common;

import com.tenten.linkhub.domain.space.model.category.Category;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

public record SpaceCursorPageRequest(
        Integer pageSize,
        Sort sort,
        Category filter
) {

    public static SpaceCursorPageRequest of(
            Integer pageSize,
            String sort,
            Category filter
            )
    {
        return new SpaceCursorPageRequest(
                pageSize,
                StringUtils.hasText(sort) ? Sort.by(sort) : Sort.unsorted(),
                filter);
    }

}
