package com.tenten.linkhub.domain.space.repository.space.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.repository.common.dto.QSpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.space.dto.MySpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndOwnerNickName;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.member.model.QMember.member;
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

    public Slice<SpaceAndOwnerNickName> findPublicSpacesJoinSpaceImageByCondition(QueryCondition condition) {
        List<SpaceAndOwnerNickName> spaceAndOwnerNickNames = queryFactory
                .select(new QSpaceAndOwnerNickName(
                        space,
                        member.nickname
                ))
                .from(space)
                .join(space.spaceImages.spaceImageList).fetchJoin()
                .join(member).on(space.memberId.eq(member.id))
                .where(space.isDeleted.eq(false),
                        space.isVisible.eq(true),
                        dynamicQueryFactory.eqSpaceName(condition.keyWord()),
                        dynamicQueryFactory.eqCategory(condition.filter())
                )
                .orderBy(dynamicQueryFactory.spaceSort(condition.pageable()))
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (spaceAndOwnerNickNames.size() > condition.pageable().getPageSize()) {
            spaceAndOwnerNickNames.remove(condition.pageable().getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(spaceAndOwnerNickNames, condition.pageable(), hasNext);
    }

    public Slice<SpaceAndOwnerNickName> findMySpacesJoinSpaceImageByCondition(MySpacesQueryCondition condition) {
        List<SpaceAndOwnerNickName> spaceAndOwnerNickNames = queryFactory
                .select(new QSpaceAndOwnerNickName(
                        space,
                        member.nickname
                ))
                .from(space)
                .leftJoin(space.spaceImages.spaceImageList).fetchJoin()
                .join(space.spaceMembers.spaceMemberList, spaceMember)
                .join(member).on(space.memberId.eq(member.id))
                .where(spaceMember.memberId.eq(condition.memberId()),
                        space.isDeleted.eq(false),
                        dynamicQueryFactory.eqSpaceName(condition.keyWord()),
                        dynamicQueryFactory.eqCategory(condition.filter())
                )
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (spaceAndOwnerNickNames.size() > condition.pageable().getPageSize()) {
            spaceAndOwnerNickNames.remove(condition.pageable().getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(spaceAndOwnerNickNames, condition.pageable(), hasNext);
    }

}
