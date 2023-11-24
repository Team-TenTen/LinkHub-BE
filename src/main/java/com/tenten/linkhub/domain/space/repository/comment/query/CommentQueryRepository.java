package com.tenten.linkhub.domain.space.repository.comment.query;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.linkhub.domain.space.model.space.QComment;
import com.tenten.linkhub.domain.space.repository.comment.dto.CommentAndChildCommentCount;
import com.tenten.linkhub.domain.space.repository.comment.dto.QCommentAndChildCommentCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tenten.linkhub.domain.space.model.space.QComment.comment;

@Repository
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CommentQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Slice<CommentAndChildCommentCount> findCommentAndChildCommentCountBySpaceId(Long spaceId, Pageable pageable){
        QComment subComment = new QComment("subComment");

        List<CommentAndChildCommentCount> responses = queryFactory
                .select(new QCommentAndChildCommentCount(
                        comment,
                        JPAExpressions
                                .select(subComment.count())
                                .from(subComment)
                                .where(subComment.groupNumber.eq(comment.id),
                                        subComment.isDeleted.eq(false))
                ))
                .from(comment)
                .where(comment.isDeleted.eq(false),
                        comment.space.id.eq(spaceId),
                        comment.parentComment.isNull())
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (responses.size() > pageable.getPageSize()) {
            responses.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(responses, pageable, hasNext);
    }

}
