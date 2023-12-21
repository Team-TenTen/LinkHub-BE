package com.tenten.linkhub.domain.space.repository.space.querydsl;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.tenten.linkhub.domain.space.model.category.Category;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static com.tenten.linkhub.domain.space.model.space.QSpace.space;

@NoArgsConstructor
public class DynamicQueryFactory {

    public OrderSpecifier<String> spaceSort(Pageable pageable) {
        for (Sort.Order sort : pageable.getSort()) {
            String property = sort.getProperty();

            switch (property) {
                case "favorite_count" -> {
                    return new OrderSpecifier(Order.DESC, space.favoriteCount);
                }
                case "created_at" -> {
                    return new OrderSpecifier(Order.DESC, space.createdAt);
                }
            }
        }

        return new OrderSpecifier(Order.ASC, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);
    }

    public BooleanExpression eqCategory(Category filter) {
        if (filter != null) {
            return space.category.eq(filter);
        }

        return null;
    }

    public BooleanExpression eqSpaceName(String keyWord) {
        if (StringUtils.hasText(keyWord)) {
            String formattedSearchWord = "\"" + keyWord + "\"";
            return numberTemplate(Double.class, "function('match_against', {0}, {1})",
                    space.spaceName, keyWord).gt(0);
        }

        return null;
    }

    public BooleanExpression eqIsVisible(Boolean isSelfSpace) {
        if (isSelfSpace) {
            return null;
        }

        return space.isVisible.eq(true);
    }

}
