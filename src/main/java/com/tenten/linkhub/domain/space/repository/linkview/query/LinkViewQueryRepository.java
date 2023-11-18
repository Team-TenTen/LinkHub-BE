package com.tenten.linkhub.domain.space.repository.linkview.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import static com.tenten.linkhub.domain.space.model.link.QLinkViewHistory.linkViewHistory;

@Repository
public class LinkViewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public LinkViewQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public boolean existsLinkViewHistory(Long linkId, Long memberId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(linkViewHistory)
                .where(equalsMemberAndLink(memberId, linkId))
                .fetchFirst();

        return fetchOne != null;
    }

    private BooleanExpression equalsMemberAndLink(Long memberId, Long linkId) {
        BooleanExpression memberCondition = linkViewHistory.memberId.eq(memberId);
        BooleanExpression linkCondition = linkViewHistory.link.id.eq(linkId);

        return memberCondition.and(linkCondition);
    }

}
