package com.tenten.linkhub.domain.space.repository.space.query;

import static com.tenten.linkhub.domain.space.model.space.QSpace.space;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class SpaceQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final DynamicQueryFactory dynamicQueryFactory;

    public SpaceQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.dynamicQueryFactory = new DynamicQueryFactory();
    }

    public Slice<Space> findSpaceWithSpaceImageByCondition(QueryCondition condition) {

        List<Space> spaces = queryFactory
                .select(space)
                .from(space)
                .join(space.spaceImages.spaceImageList).fetchJoin()
                .where(space.isDeleted.eq(false),
                        dynamicQueryFactory.eqSpaceName(condition.keyWord()),
                        dynamicQueryFactory.eqCategory(condition.filter())
                )
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
