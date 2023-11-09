package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.comment.CommentRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.comment.RootCommentCreateRequest;
import com.tenten.linkhub.domain.space.service.mapper.CommentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final SpaceRepository spaceRepository;
    private final CommentMapper mapper;

    public CommentService(CommentRepository commentRepository, SpaceRepository spaceRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.spaceRepository = spaceRepository;
        this.mapper = commentMapper;
    }

    @Transactional
    public Long createComment(RootCommentCreateRequest request) {
        Space space = spaceRepository.getById(request.spaceId());

        space.validateCommentAvailability();

        Comment comment = mapper.toComment(request, space);
        return commentRepository.save(comment).getId();
    }

}
