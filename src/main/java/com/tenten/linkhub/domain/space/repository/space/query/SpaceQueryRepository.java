package com.tenten.linkhub.domain.space.repository.space.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.repository.space.dto.QSpaceWithSpaceImage;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceWithSpaceImage;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.tenten.linkhub.domain.space.model.space.QSpace.space;
import static com.tenten.linkhub.domain.space.model.space.QSpaceImage.spaceImage;

@Repository
public class SpaceQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final DynamicQueryFactory dynamicQueryFactory;

    public SpaceQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.dynamicQueryFactory = new DynamicQueryFactory();
    }

    public Slice<SpaceWithSpaceImage> findSpaceWithSpaceImageByCondition(QueryCondition condition) {

        List<SpaceWithSpaceImage> spaces = queryFactory
                .select(new QSpaceWithSpaceImage(
                        space,
                        spaceImage
                ))
                .from(space)
                .join(spaceImage).on(space.id.eq(spaceImage.space.id))
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

    public Optional<SpaceWithSpaceImage> findSpaceWithSpaceImageById(Long spaceId) {
        return Optional.ofNullable(queryFactory
                .select(new QSpaceWithSpaceImage(
                        space,
                        spaceImage
                ))
                .from(space)
                .where(space.id.eq(spaceId))
                .join(spaceImage).on(space.id.eq(spaceImage.space.id))
                .fetchOne());
    }

}
