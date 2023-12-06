package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.service.dto.comment.ReplyCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.RootCommentCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toComment(RootCommentCreateRequest request, Space space){
        return new Comment(
                null,
                null,
                request.content(),
                request.memberId(),
                space);
    }

    public Comment toReply(ReplyCreateRequest request, Space space, Comment parentComment, Long groupId){
        return new Comment(
                parentComment,
                groupId,
                request.content(),
                request.memberId(),
                space);
    }

}
