package com.tenten.linkhub.domain.space.repository.space.querydsl;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.global.util.SearchKeywordParser;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static com.tenten.linkhub.domain.space.model.space.QSpace.space;

@NoArgsConstructor
@Component
public class DynamicQueryFactory {

    public BooleanExpression ltLastFavoriteCountAndId(Long lastFavoriteCount, Long lastId, Sort sort) {
        String requestSort = getRequestSort(sort);

        if (isCreatedAtRequest(requestSort, lastId)) {
            return space.id.lt(lastId);
        }

        if (isFavoriteCountRequest(requestSort, lastFavoriteCount, lastId)) {
            return space.favoriteCount.lt(lastFavoriteCount)
                    .or(space.favoriteCount.eq(lastFavoriteCount).and(space.id.lt(lastId)));
        }

        return null;
    }

    public OrderSpecifier<String>[] spaceSort(Sort sort) {
        String property = getRequestSort(sort);

        if (Objects.isNull(property)) {
            return null;
        }

        switch (property) {
            case "favorite_count" -> {
                return new OrderSpecifier[]{
                        new OrderSpecifier(Order.DESC, space.favoriteCount),
                        new OrderSpecifier(Order.DESC, space.id)
                };
            }

            case "created_at" -> {
                return new OrderSpecifier[]{
                        new OrderSpecifier(Order.DESC, space.id)
                };
            }
        }

        return null;
    }

    public BooleanExpression eqCategory(Category filter) {
        if (filter != null) {
            return space.category.eq(filter);
        }

        return null;
    }

    public BooleanExpression eqSpaceName(String keyWord) {
        if (StringUtils.hasText(keyWord)) {
            String tokenizedKeyword = SearchKeywordParser.parseToTokenAndAppendPlus(keyWord, 2);

            return numberTemplate(
                    Double.class,
                    "function('match_against', {0}, {1})",
                    space.spaceName,
                    tokenizedKeyword)
                    .gt(0);
        }

        return null;
    }

    public BooleanExpression eqSpaceNameWithPlus(String keyWord) {
        if (StringUtils.hasText(keyWord)) {
            String tokenizedKeyword = SearchKeywordParser.parseToTokenAndAppendPlus(keyWord, 2);

            return numberTemplate(
                    Double.class,
                    "function('match_against', {0}, {1})",
                    space.spaceName,
                    tokenizedKeyword)
                    .gt(0);
        }

        return null;
    }

    public BooleanExpression eqSpaceNameWithLikeQuery(String keyWord) {
        if (StringUtils.hasText(keyWord)) {
            return space.spaceName.contains(keyWord);
        }

        return null;
    }

    public BooleanExpression eqIsVisible(Boolean isSelfSpace) {
        if (isSelfSpace) {
            return null;
        }

        return space.isVisible.eq(true);
    }

    private String getRequestSort(Sort sort) {
        return sort.stream()
                .findFirst()
                .map(Sort.Order::getProperty)
                .orElse(null);
    }

    private boolean isCreatedAtRequest(String requestSort, Long lastId) {
        return Objects.equals(requestSort, "created_at") &&
                Objects.nonNull(lastId);
    }

    private boolean isFavoriteCountRequest(String requestSort, Long lastFavoriteCount, Long lastId) {
        return Objects.equals(requestSort, "favorite_count") &&
                Objects.nonNull(lastFavoriteCount) &&
                Objects.nonNull(lastId);
    }

}
