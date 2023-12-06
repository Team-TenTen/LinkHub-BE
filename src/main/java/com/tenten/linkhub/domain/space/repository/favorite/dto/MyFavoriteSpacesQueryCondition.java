package com.tenten.linkhub.domain.space.repository.favorite.dto;

import com.tenten.linkhub.domain.space.model.category.Category;
import org.springframework.data.domain.Pageable;

public record MyFavoriteSpacesQueryCondition(
        Pageable pageable,
        String keyWord,
        Category filter,
        Long memberId
) {
}
