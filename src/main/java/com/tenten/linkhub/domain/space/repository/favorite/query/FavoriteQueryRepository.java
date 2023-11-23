package com.tenten.linkhub.domain.space.repository.favorite.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.repository.common.dto.QSpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickName;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickNames;
import com.tenten.linkhub.domain.space.repository.favorite.dto.MyFavoriteSpacesQueryCondition;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.tenten.linkhub.domain.member.model.QMember.member;
import static com.tenten.linkhub.domain.space.model.space.QFavorite.favorite;
import static com.tenten.linkhub.domain.space.model.space.QSpaceImage.spaceImage;

@Repository
public class FavoriteQueryRepository {

    private final JPAQueryFactory queryFactory;

    public FavoriteQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Slice<SpaceAndSpaceImageOwnerNickName> findMyFavoriteSpacesByQuery(MyFavoriteSpacesQueryCondition condition) {
        List<SpaceAndOwnerNickName> spaceAndOwnerNickNames = queryFactory
                .select(new QSpaceAndOwnerNickName(
                        favorite.space,
                        member.nickname
                ))
                .from(favorite)
                .join(favorite.space)
                .leftJoin(member).on(favorite.space.memberId.eq(member.id))
                .where(favorite.memberId.eq(condition.memberId()),
                        favorite.space.isDeleted.eq(false),
                        eqSpaceName(condition.keyWord()),
                        eqCategory(condition.filter())
                )
                .orderBy(favorite.createdAt.desc())
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

    private BooleanExpression eqCategory(Category filter) {
        if (Objects.nonNull(filter)){
            return favorite.space.category.eq(filter);
        }

        return null;
    }

    private BooleanExpression eqSpaceName(String keyWord) {
        if (StringUtils.hasText(keyWord)) {
            return favorite.space.spaceName.contains(keyWord);
        }

        return null;
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
