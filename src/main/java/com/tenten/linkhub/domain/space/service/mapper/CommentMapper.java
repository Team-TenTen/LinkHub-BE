package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.model.space.Space;
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

}
