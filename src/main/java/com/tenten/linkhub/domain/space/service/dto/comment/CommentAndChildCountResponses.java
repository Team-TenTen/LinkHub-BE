package com.tenten.linkhub.domain.space.service.dto.comment;

import com.tenten.linkhub.domain.space.repository.comment.dto.CommentAndChildCommentCount;
import org.springframework.data.domain.Slice;

public record CommentAndChildCountResponses(Slice<CommentAndChildCountDto> responses) {

    public static CommentAndChildCountResponses from(Slice<CommentAndChildCommentCount> responses){
        Slice<CommentAndChildCountDto> mapResponses = responses.map(c -> new CommentAndChildCountDto(
                c.comment().getId(),
                c.comment().getContent(),
                c.comment().getCreatedAt(),
                c.comment().getUpdatedAt(),
                c.comment().getMemberId(),
                c.childCommentCount()
        ));

        return new CommentAndChildCountResponses(mapResponses);
    }

}
