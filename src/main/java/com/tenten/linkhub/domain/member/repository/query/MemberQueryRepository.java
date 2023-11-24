package com.tenten.linkhub.domain.member.repository.query;

import static com.tenten.linkhub.domain.member.model.QFollow.follow;
import static com.tenten.linkhub.domain.member.model.QMember.member;
import static com.tenten.linkhub.domain.member.model.QProfileImage.profileImage;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.member.repository.dto.MemberSearchQueryCondition;
import com.tenten.linkhub.domain.member.repository.dto.MemberWithProfileImageAndFollowingStatus;
import com.tenten.linkhub.domain.member.repository.dto.QMemberWithProfileImageAndFollowingStatus;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MemberQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Slice<MemberWithProfileImageAndFollowingStatus> findMembersWithProfileImages(
            MemberSearchQueryCondition queryCond) {

        long dummyId = -1L;

        BooleanExpression isFollowing = follow.following.id.isNotNull();

        BooleanExpression joinCondition = follow.following.id.eq(
                Expressions.constant(queryCond.myMemberId() != null ? queryCond.myMemberId() : dummyId)
        );

        List<MemberWithProfileImageAndFollowingStatus> memberWithProfileImageAndFollowingStatus = queryFactory
                .select(new QMemberWithProfileImageAndFollowingStatus(
                        member,
                        isFollowing
                ))
                .from(member)
                .leftJoin(member.profileImages.profileImageList, profileImage)
                .leftJoin(follow).on(member.id.eq(follow.follower.id), joinCondition)
                .where(member.isDeleted.eq(false),
                        profileImage.isDeleted.eq(false),
                        member.nickname.startsWith(queryCond.keyword())
                )
                .offset(queryCond.pageable().getOffset())
                .limit(queryCond.pageable().getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (memberWithProfileImageAndFollowingStatus.size() > queryCond.pageable().getPageSize()) {
            memberWithProfileImageAndFollowingStatus.remove(queryCond.pageable().getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(memberWithProfileImageAndFollowingStatus, queryCond.pageable(), hasNext);
    }
}
