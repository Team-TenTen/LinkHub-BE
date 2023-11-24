package com.tenten.linkhub.domain.space.repository.link.querydsl;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.repository.link.dto.LinkGetDto;
import com.tenten.linkhub.domain.space.repository.link.dto.LinkGetQueryCondition;
import com.tenten.linkhub.domain.space.repository.link.dto.LinkViewDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.member.model.QMember.member;
import static com.tenten.linkhub.domain.member.model.QProfileImage.profileImage;
import static com.tenten.linkhub.domain.space.model.link.QLike.like;
import static com.tenten.linkhub.domain.space.model.link.QLink.link;
import static com.tenten.linkhub.domain.space.model.link.QLinkTag.linkTag;
import static com.tenten.linkhub.domain.space.model.link.QLinkViewHistory.linkViewHistory;
import static com.tenten.linkhub.domain.space.model.link.QTag.tag;
import static com.tenten.linkhub.domain.space.model.space.QSpace.space;


@Repository
public class LinkQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public LinkQueryDslRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Slice<LinkGetDto> getLinksByCondition(LinkGetQueryCondition condition) {
        List<LinkGetDto> linkGetDtos = jpaQueryFactory
                .select(link)
                .from(link)
                .leftJoin(link.linkTags, linkTag).on(linkTag.isDeleted.eq(false))
                .leftJoin(linkTag.tag, tag)
                .leftJoin(link.space, space)
                .leftJoin(link.linkViewHistories, linkViewHistory)
                .leftJoin(member).on(linkViewHistory.memberId.eq(member.id))
                .leftJoin(member.profileImages.profileImageList, profileImage)
                .leftJoin(like).on(like.link.eq(link))
                .where(link.space.id.eq(condition.spaceId()))
                .where(link.isDeleted.eq(Boolean.FALSE))
                .where(hasTagFilter(condition.tagId()))
                .orderBy(linkSort(condition.pageable()))
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize() + 1)
                .transform(
                        GroupBy.groupBy(link.id).list(
                                Projections.fields(
                                        LinkGetDto.class,
                                        link.id.as("linkId"),
                                        link.title.as("title"),
                                        link.url.url.as("url"),
                                        tag.name.as("tagName"),
                                        tag.color.as("tagColor"),
                                        link.likeCount.as("likeCount"),
                                        isLikedByMember(condition.memberId()).as("isLiked"),
                                        space.isLinkSummarizable.as("canLinkSummaraizable"),
                                        space.isReadMarkEnabled.as("canReadMark"),
                                        GroupBy.list(
                                                Projections.fields(
                                                        LinkViewDto.class,
                                                        member.nickname.as("memberName"),
                                                        profileImage.path.as("memberProfileImage")
                                                )
                                        ).as("linkViewHistories")
                                )
                        )
                );


        boolean hasNext = false;

        if (linkGetDtos.size() > condition.pageable().getPageSize()) {
            linkGetDtos.remove(condition.pageable().getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(linkGetDtos, condition.pageable(), hasNext);
    }

    private BooleanExpression isLikedByMember(Long memberId) {
        if (memberId != null) {
            return like.memberId.eq(memberId);
        }
        return Expressions.FALSE;
    }

    private BooleanExpression hasTagFilter(Long tagId) {
        if (tagId != null) {
            return linkTag.tag.id.eq(tagId);
        }
        return Expressions.TRUE;
    }


    private OrderSpecifier<String> linkSort(Pageable pageable) {
        for (Sort.Order sort : pageable.getSort()) {
            String property = sort.getProperty();

            switch (property) {
                case "created_at" -> {
                    return new OrderSpecifier(Order.DESC, link.createdAt);
                }
                case "popular" -> {
                    return new OrderSpecifier(Order.DESC, link.likeCount);
                }
            }
        }

        // 정렬 조건이 없는 경우 기본적으로 최신순(createdAt 내림차순)으로 정렬
        return new OrderSpecifier(Order.DESC, link.createdAt);
    }

}
