package com.tenten.linkhub.domain.space.repository.space.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.MySpacesFindQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.space.model.space.QSpace.space;
import static com.tenten.linkhub.domain.space.model.space.QSpaceMember.spaceMember;

@Repository
public class SpaceQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final DynamicQueryFactory dynamicQueryFactory;

    public SpaceQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.dynamicQueryFactory = new DynamicQueryFactory();
    }

    public Slice<Space> findSpacesJoinSpaceImageByCondition(QueryCondition condition) {
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

    public Slice<Space> findMySpacesJoinSpaceImageByCondition(MySpacesFindQueryCondition condition) {
        List<Space> spaces = queryFactory
                .select(space)
                .from(space)
                .join(space.spaceMembers.spaceMemberList, spaceMember)
                .join(space.spaceImages.spaceImageList).fetchJoin()
                .where(spaceMember.memberId.eq(condition.memberId()),
                        space.isDeleted.eq(false),
                        dynamicQueryFactory.eqSpaceName(condition.keyWord()),
                        dynamicQueryFactory.eqCategory(condition.filter())
                )
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
