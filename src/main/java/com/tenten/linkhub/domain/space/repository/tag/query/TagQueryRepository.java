package com.tenten.linkhub.domain.space.repository.tag.query;

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

    public List<TagInfo> findTagBySpaceIdAndGroupBySpaceName(Long spaceId) {
        return jpaQueryFactory
                .select(new QTagInfo(
                        tag.name,
                        tag.color
                ))
                .from(tag)
                .where(tag.space.id.eq(spaceId))
                .groupBy(tag.name, tag.color)
                .fetch();
    }

}
