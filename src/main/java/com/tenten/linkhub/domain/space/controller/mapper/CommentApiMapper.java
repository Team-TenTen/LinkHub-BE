package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.service.dto.comment.CommentCreateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CommentApiMapper {

    CommentCreateRequest toCommentCreateRequest(Long spaceId, Long memberId, String content);
}
