package com.tenten.linkhub.domain.space.service.dto.comment;

import com.tenten.linkhub.domain.space.model.space.Comment;
import org.springframework.data.domain.Slice;

public record RepliesFindResponses(Slice<RepliesFindResponse> responses) {

    public static RepliesFindResponses from(Slice<Comment> responses) {
        Slice<RepliesFindResponse> repliesFindResponses = responses.map(c -> new RepliesFindResponse(
                c.getId(),
                c.getContent(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getMemberId(),
                c.getGroupNumber(),
                c.getParentComment().getId()
        ));

        return new RepliesFindResponses(repliesFindResponses);
    }

}