package com.tenten.linkhub.domain.space.repository.space;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.space.model.space.QSpace.space;

@Repository
public class SpaceQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final DynamicQueryFactory dynamicQueryFactory;

    public SpaceQueryRepository(JPAQueryFactory queryFactory, DynamicQueryFactory dynamicQueryFactory) {
        this.queryFactory = queryFactory;
        this.dynamicQueryFactory = dynamicQueryFactory;
    }

    public Slice<Space> findByCondition(QueryCondition condition){

        List<Space> spaces = queryFactory
                .select(space)
                .from(space)
                .where(space.isDeleted.eq(false),
                        dynamicQueryFactory.eqSpaceName(condition.keyWord()),
                        dynamicQueryFactory.eqCategory(condition.filter())
                )
                .orderBy()
                .orderBy(dynamicQueryFactory.spaceSort(condition.pageable()))
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

}
