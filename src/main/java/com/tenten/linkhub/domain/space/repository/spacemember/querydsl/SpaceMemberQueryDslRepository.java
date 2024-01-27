package com.tenten.linkhub.domain.space.repository.spacemember.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.space.Role;
import org.springframework.stereotype.Repository;

import static com.tenten.linkhub.domain.space.model.space.QSpaceMember.spaceMember;

@Repository
public class SpaceMemberQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public SpaceMemberQueryDslRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public boolean existsAuthorizedSpaceMember(Long memberId, Long spaceId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(spaceMember)
                .where(isValidSpaceMember(memberId, spaceId),
                        hasAuthorizedRole()
                )
                .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression isValidSpaceMember(Long memberId, Long spaceId) {
        BooleanExpression memberCondition = spaceMember.memberId.eq(memberId);
        BooleanExpression spaceCondition = spaceMember.space.id.eq(spaceId);

        return memberCondition.and(spaceCondition);
    }

    private BooleanExpression hasAuthorizedRole() {
        return spaceMember.role.in(Role.OWNER, Role.CAN_EDIT);
    }

}
