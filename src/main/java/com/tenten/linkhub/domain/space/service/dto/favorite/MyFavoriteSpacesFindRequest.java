package com.tenten.linkhub.domain.space.service.dto.favorite;

import com.tenten.linkhub.domain.space.model.category.Category;
import org.springframework.data.domain.Pageable;

public record MyFavoriteSpacesFindRequest(
        Pageable pageable,
        String keyWord,
        Category filter,
        Long memberId
) {
}
