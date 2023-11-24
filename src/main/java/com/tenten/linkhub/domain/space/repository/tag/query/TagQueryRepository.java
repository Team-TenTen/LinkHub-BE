package com.tenten.linkhub.domain.space.repository.tag.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.repository.tag.dto.QTagInfo;
import com.tenten.linkhub.domain.space.repository.tag.dto.TagInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.space.model.link.QTag.tag;

@Repository
public class TagQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public TagQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<TagInfo> findTagBySpaceId(Long spaceId) {
        return jpaQueryFactory
                .select(new QTagInfo(
                        tag.name,
                        tag.color,
                        tag.id
                ))
                .from(tag)
                .where(tag.space.id.eq(spaceId))
                .fetch();
    }

    public boolean existsTagInSpace(Long spaceId, String tagName) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(tag)
                .where(equalsSpaceIdAndTagName(spaceId, tagName))
                .fetchFirst();

        return fetchOne != null;
    }

    private BooleanExpression equalsSpaceIdAndTagName(Long spaceId, String tagName) {
        BooleanExpression spaceCondition = tag.space.id.eq(spaceId);
        BooleanExpression tagNameCondition = tag.name.eq(tagName);

        return spaceCondition.and(tagNameCondition);
    }

}
