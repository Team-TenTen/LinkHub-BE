package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.service.dto.comment.CommentUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.ReplyCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.RootCommentCreateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CommentApiMapper {

    RootCommentCreateRequest toRootCommentCreateRequest(Long spaceId, Long memberId, String content);

    ReplyCreateRequest toReplyCreateRequest(Long spaceId, Long commentId, Long memberId, String content);

    CommentUpdateRequest toCommentUpdateRequest(Long spaceId, Long commentId, Long memberId, String content);
}
