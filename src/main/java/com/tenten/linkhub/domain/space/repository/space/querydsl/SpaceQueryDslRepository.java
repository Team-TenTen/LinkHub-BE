package com.tenten.linkhub.domain.space.repository.space.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImage;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImages;
import com.tenten.linkhub.domain.space.repository.space.dto.MemberSpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.space.model.space.QSpace.space;
import static com.tenten.linkhub.domain.space.model.space.QSpaceImage.spaceImage;
import static com.tenten.linkhub.domain.space.model.space.QSpaceMember.spaceMember;

@Repository
public class SpaceQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final DynamicQueryFactory dynamicQueryFactory;

    public SpaceQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.dynamicQueryFactory = new DynamicQueryFactory();
    }

    public Slice<SpaceAndSpaceImage> findPublicSpacesJoinSpaceImageByCondition(QueryCondition condition) {
        List<Space> spaces = queryFactory
                .select(space)
                .from(space)
                .where(space.isDeleted.eq(false),
                        space.isVisible.eq(true),
                        dynamicQueryFactory.eqSpaceName(condition.keyWord()),
                        dynamicQueryFactory.eqCategory(condition.filter())
                )
                .orderBy(dynamicQueryFactory.spaceSort(condition.pageable()))
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize() + 1)
                .fetch();

        List<Long> spaceIds = getSpaceIds(spaces);

        List<SpaceImage> spaceImages = findSpaceImagesBySpaceIds(spaceIds);

        SpaceAndSpaceImages spaceAndSpaceImages = SpaceAndSpaceImages.of(spaces, spaceImages);

        List<SpaceAndSpaceImage> contents = spaceAndSpaceImages.contents();
        boolean hasNext = false;

        if (contents.size() > condition.pageable().getPageSize()) {
            contents.remove(condition.pageable().getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, condition.pageable(), hasNext);
    }

    public Slice<SpaceAndSpaceImage> findMemberSpacesJoinSpaceImageByCondition(MemberSpacesQueryCondition condition) {
        List<Space> spaces = queryFactory
                .select(space)
                .from(space)
                .join(space.spaceMembers.spaceMemberList, spaceMember)
                .where(spaceMember.memberId.eq(condition.memberId()),
                        space.isDeleted.eq(false),
                        dynamicQueryFactory.eqIsVisible(condition.isMySpace()),
                        dynamicQueryFactory.eqSpaceName(condition.keyWord()),
                        dynamicQueryFactory.eqCategory(condition.filter())
                )
                .orderBy(space.createdAt.desc())
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize() + 1)
                .fetch();

        List<Long> spaceIds = getSpaceIds(spaces);

        List<SpaceImage> spaceImages = findSpaceImagesBySpaceIds(spaceIds);

        SpaceAndSpaceImages spaceAndSpaceImages = SpaceAndSpaceImages.of(spaces, spaceImages);

        List<SpaceAndSpaceImage> contents = spaceAndSpaceImages.contents();
        boolean hasNext = false;

        if (contents.size() > condition.pageable().getPageSize()) {
            contents.remove(condition.pageable().getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, condition.pageable(), hasNext);
    }

    private List<SpaceImage> findSpaceImagesBySpaceIds(List<Long> spaceIds) {
        return queryFactory
                .selectFrom(spaceImage)
                .where(spaceImage.space.id.in(spaceIds),
                        spaceImage.isDeleted.eq(false))
                .fetch();
    }

    private static List<Long> getSpaceIds(List<Space> spaces) {
        return spaces
                .stream()
                .map(Space::getId)
                .toList();
    }

}
