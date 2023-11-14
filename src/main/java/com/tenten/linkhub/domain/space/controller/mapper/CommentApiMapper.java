package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.service.dto.comment.RootCommentCreateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CommentApiMapper {

    RootCommentCreateRequest toRootCommentCreateRequest(Long spaceId, Long memberId, String content);

}
