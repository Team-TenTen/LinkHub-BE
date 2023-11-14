package com.tenten.linkhub.domain.space.service.dto.comment;

import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.repository.comment.dto.CommentAndChildCommentCount;
import org.springframework.data.domain.Slice;

public record RepliesFindResponses(Slice<RepliesFindResponse> responses) {

    public static RepliesFindResponses from(Slice<Comment> responses) {
        Slice<RepliesFindResponse> repliesFindResponses = responses.map(c -> new RepliesFindResponse(
                c.getId(),
                c.getContent(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getMemberId()
        ));

        return new RepliesFindResponses(repliesFindResponses);
    }

}