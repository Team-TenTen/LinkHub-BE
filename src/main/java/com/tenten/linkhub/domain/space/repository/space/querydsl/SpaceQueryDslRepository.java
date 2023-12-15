package com.tenten.linkhub.domain.space.repository.space.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.repository.common.dto.QSpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickName;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickNames;
import com.tenten.linkhub.domain.space.repository.space.dto.MemberSpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.member.model.QMember.member;
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

    public Slice<SpaceAndSpaceImageOwnerNickName> findPublicSpacesJoinSpaceImageByCondition(QueryCondition condition) {
        List<SpaceAndOwnerNickName> spaceAndOwnerNickNames = queryFactory
                .select(new QSpaceAndOwnerNickName(
                        space,
                        member.nickname
                ))
                .from(space)
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

        List<Long> spaceIds = getSpaceIds(spaceAndOwnerNickNames);

        List<SpaceImage> spaceImages = findSpaceImagesBySpaceIds(spaceIds);

        SpaceAndSpaceImageOwnerNickNames spaceAndSpaceImageOwnerNickNames = SpaceAndSpaceImageOwnerNickNames.of(spaceAndOwnerNickNames, spaceImages);

        List<SpaceAndSpaceImageOwnerNickName> contents = spaceAndSpaceImageOwnerNickNames.contents();
        boolean hasNext = false;

        if (contents.size() > condition.pageable().getPageSize()) {
            contents.remove(condition.pageable().getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, condition.pageable(), hasNext);
    }

    public Slice<SpaceAndSpaceImageOwnerNickName> findMemberSpacesJoinSpaceImageByCondition(MemberSpacesQueryCondition condition) {
        List<SpaceAndOwnerNickName> spaceAndOwnerNickNames = queryFactory
                .select(new QSpaceAndOwnerNickName(
                        space,
                        member.nickname
                ))
                .from(space)
                .join(space.spaceMembers.spaceMemberList, spaceMember)
                .join(member).on(space.memberId.eq(member.id))
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

        List<Long> spaceIds = getSpaceIds(spaceAndOwnerNickNames);

        List<SpaceImage> spaceImages = findSpaceImagesBySpaceIds(spaceIds);

        SpaceAndSpaceImageOwnerNickNames spaceAndSpaceImageOwnerNickNames = SpaceAndSpaceImageOwnerNickNames.of(spaceAndOwnerNickNames, spaceImages);

        List<SpaceAndSpaceImageOwnerNickName> contents = spaceAndSpaceImageOwnerNickNames.contents();
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

    private static List<Long> getSpaceIds(List<SpaceAndOwnerNickName> spaceAndOwnerNickNames) {
        return spaceAndOwnerNickNames
                .stream()
                .map(s -> s.space().getId())
                .toList();
    }

}
