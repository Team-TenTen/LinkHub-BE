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
import com.tenten.linkhub.domain.space.repository.link.dto.LinkInfoDto;
import com.tenten.linkhub.domain.space.repository.link.dto.LinkViewDto;
import com.tenten.linkhub.domain.space.repository.link.dto.PopularLinkGetDto;
import com.tenten.linkhub.domain.space.repository.link.dto.QLinkInfoDto;
import com.tenten.linkhub.domain.space.repository.link.dto.QPopularLinkGetDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.types.dsl.Expressions.TRUE;
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
        List<LinkInfoDto> linkInfoDtos = jpaQueryFactory
                .select(
                        new QLinkInfoDto(
                                link.id,
                                link.title,
                                link.url.url,
                                tag.name,
                                tag.color,
                                link.likeCount,
                                isLikedByMember(condition.memberId()),
                                space.isLinkSummarizable,
                                space.isReadMarkEnabled,
                                link.createdAt
                        )
                )
                .from(link)
                .leftJoin(link.linkTags, linkTag).on(linkTag.isDeleted.eq(false))
                .leftJoin(linkTag.tag, tag)
                .leftJoin(link.space, space)
                .leftJoin(like).on(like.link.eq(link), isLikedByMember(condition.memberId()))
                .where(link.space.id.eq(condition.spaceId()))
                .where(link.isDeleted.eq(Boolean.FALSE))
                .where(hasTagFilter(condition.tagId()))
                .orderBy(linkSort(condition.pageable()))
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize() + 1)
                .distinct()
                .fetch();

        boolean hasNext = false;

        if (linkInfoDtos.size() > condition.pageable().getPageSize()) {
            linkInfoDtos.remove(condition.pageable().getPageSize());
            hasNext = true;
        }

        List<Long> linkIds = linkInfoDtos.stream()
                .map(LinkInfoDto::linkId) // LinkInfoDto 객체에서 id 필드만 추출
                .toList();

        Map<Long, List<LinkViewDto>> linkViewDtoMap = jpaQueryFactory
                .select(link)
                .from(link)
                .leftJoin(link.linkViewHistories, linkViewHistory)
                .leftJoin(member).on(linkViewHistory.memberId.eq(member.id))
                .leftJoin(member.profileImages.profileImageList, profileImage)
                .where(link.id.in(linkIds))
                .transform(GroupBy.groupBy(link.id).as(
                        GroupBy.list(
                                Projections.fields(
                                        LinkViewDto.class,
                                        member.nickname.as("memberName"),
                                        profileImage.path.as("memberProfileImage")
                                ).skipNulls()
                        )

                ));


        // LinkInfoDto와 LinkViewDtoMap을 사용하여 LinkGetDto 생성
        List<LinkGetDto> linkGetDtos =
                linkInfoDtos.stream()
                        .map(linkInfoDto -> new LinkGetDto(linkInfoDto, linkViewDtoMap.get(linkInfoDto.linkId())))
                        .toList();

        return new SliceImpl<>(linkGetDtos, condition.pageable(), hasNext);
    }

    public List<PopularLinkGetDto> getPopularLinks(Long memberId) {
        return jpaQueryFactory
                .select(
                        new QPopularLinkGetDto(
                                link.id,
                                link.title,
                                link.url.url,
                                tag.name,
                                tag.color,
                                link.likeCount,
                                isLikedByMember(memberId)
                        )
                )
                .from(link)
                .leftJoin(link.space, space)
                .leftJoin(link.linkTags, linkTag).on(linkTag.isDeleted.eq(false))
                .leftJoin(linkTag.tag, tag)
                .leftJoin(like).on(like.link.eq(link), isLikedByMember(memberId))
                .where(link.isDeleted.eq(Boolean.FALSE),
                        space.isDeleted.eq(Boolean.FALSE))
                .orderBy(link.likeCount.desc())
                .limit(5)
                .distinct()
                .fetch();
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
        return TRUE;
    }


    private OrderSpecifier[] linkSort(Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        for (Sort.Order sort : pageable.getSort()) {
            String property = sort.getProperty();

            switch (property) {
                case "created_at" -> {
                    orderSpecifiers.add(new OrderSpecifier(Order.DESC, link.createdAt));
                    orderSpecifiers.add(new OrderSpecifier(Order.DESC, link.id));
                    return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
                }
                case "popular" -> {
                    orderSpecifiers.add(new OrderSpecifier(Order.DESC, link.likeCount));
                    return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
                }
            }
        }

        // 정렬 조건이 없는 경우 기본적으로 최신순(createdAt 내림차순)으로 정렬
        orderSpecifiers.add(new OrderSpecifier(Order.DESC, link.createdAt));
        orderSpecifiers.add(new OrderSpecifier(Order.DESC, link.id));
        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

}
