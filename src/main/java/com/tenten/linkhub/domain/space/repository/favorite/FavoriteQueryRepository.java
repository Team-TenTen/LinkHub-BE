package com.tenten.linkhub.domain.space.repository.favorite;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.favorite.dto.MyFavoriteSpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QSpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.space.query.DynamicQueryFactory;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.tenten.linkhub.domain.member.model.QMember.member;
import static com.tenten.linkhub.domain.space.model.space.QFavorite.favorite;

@Repository
public class FavoriteQueryRepository {

    private final JPAQueryFactory queryFactory;

    public FavoriteQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    public Slice<SpaceAndOwnerNickName> findMyFavoriteSpacesByQuery(MyFavoriteSpacesQueryCondition condition) {
        List<SpaceAndOwnerNickName> spaceAndOwnerNickNames = queryFactory
                .select(new QSpaceAndOwnerNickName(
                        favorite.space,
                        member.nickname
                ))
                .from(favorite)
                .join(favorite.space).fetchJoin()
                .leftJoin(favorite.space.spaceImages.spaceImageList).fetchJoin()
                .join(member).on(favorite.memberId.eq(member.id))
                .where(favorite.memberId.eq(condition.memberId()),
                        favorite.space.isDeleted.eq(false),
                        eqSpaceName(condition.keyWord()),
                        eqCategory(condition.filter())
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

}
