package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.comment.CommentRepository;
import com.tenten.linkhub.domain.space.repository.comment.dto.CommentAndChildCommentCount;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentAndChildCountResponses;
import com.tenten.linkhub.domain.space.service.dto.comment.RootCommentCreateRequest;
import com.tenten.linkhub.domain.space.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final SpaceRepository spaceRepository;
    private final CommentMapper mapper;

    @Transactional
    public Long createComment(RootCommentCreateRequest request) {
        Space space = spaceRepository.getById(request.spaceId());

        space.validateCommentAvailability();

        Comment comment = mapper.toComment(request, space);
        return commentRepository.save(comment).getId();
    }

    @Transactional(readOnly = true)
    public CommentAndChildCountResponses findRootComments(Long spaceId, Pageable pageable) {
        Space space = spaceRepository.getById(spaceId);
        space.validateCommentAvailability();

        Slice<CommentAndChildCommentCount> responses = commentRepository.findCommentAndChildCommentCountBySpaceId(spaceId, pageable);

        return CommentAndChildCountResponses.from(responses);
    }

}
