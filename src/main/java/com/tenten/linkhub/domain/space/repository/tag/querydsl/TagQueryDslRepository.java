package com.tenten.linkhub.domain.space.repository.tag.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.repository.tag.dto.QTagInfo;
import com.tenten.linkhub.domain.space.repository.tag.dto.TagInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.space.model.link.QLinkTag.linkTag;
import static com.tenten.linkhub.domain.space.model.link.QTag.tag;
import static java.lang.Boolean.FALSE;

@Repository
public class TagQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public TagQueryDslRepository(JPAQueryFactory jpaQueryFactory) {
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
                .join(linkTag).on(linkTag.tag.eq(tag))
                .groupBy(tag)
                .where(tag.space.id.eq(spaceId), isActiveTag())
                .fetch();
    }

    private BooleanExpression isActiveTag() {
        return linkTag.isDeleted.eq(FALSE);
    }
}
