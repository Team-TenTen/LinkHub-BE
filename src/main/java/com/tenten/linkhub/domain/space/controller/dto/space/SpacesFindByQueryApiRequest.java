package com.tenten.linkhub.domain.space.controller.dto.space;

import com.tenten.linkhub.domain.space.model.category.Category;

public record SpacesFindByQueryApiRequest(
        String keyWord,
        Category filter
) {

}
