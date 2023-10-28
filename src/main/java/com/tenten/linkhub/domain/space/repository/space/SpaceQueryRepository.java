package com.tenten.linkhub.domain.space.repository.space;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCond;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.tenten.linkhub.domain.space.model.space.QSpace.space;

@Repository
public class SpaceQueryRepository {

    private final JPAQueryFactory queryFactory;

    public SpaceQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Slice<Space> findByCondition(QueryCond condition){
        List<Space> spaces = queryFactory
                .select(space)
                .from(space)
                .where(space.isDeleted.eq(false),
                        eqSpaceName(condition.keyWord()),
                        eqCategory(condition.filter())
                )
                .orderBy(spaceSort(condition.pageable()))
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (spaces.size() > condition.pageable().getPageSize()) {
            spaces.remove(condition.pageable().getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(spaces, condition.pageable(), hasNext);
    }

    private OrderSpecifier<?> spaceSort(Pageable pageable) {
        for (Sort.Order sort: pageable.getSort()){
            String property = sort.getProperty();

            switch (property){
                case "favorite_count" -> {
                    return new OrderSpecifier(Order.DESC, space.favoriteCount);
                }
                case "created_at"-> {
                    return new OrderSpecifier(Order.DESC, space.createdAt);
                }
            }
        }

        return null;
    }

    private BooleanExpression eqCategory(Category filter) {
        if (filter != null){
            return space.category.eq(filter);
        }

        return null;
    }

    private BooleanExpression eqSpaceName(String keyWord) {
        if (StringUtils.hasText(keyWord)){
            return space.spaceName.contains(keyWord);
        }

        return null;
    }

}
