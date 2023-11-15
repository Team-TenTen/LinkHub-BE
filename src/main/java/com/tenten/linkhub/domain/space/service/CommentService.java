package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.comment.CommentRepository;
import com.tenten.linkhub.domain.space.repository.comment.dto.CommentAndChildCommentCount;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentAndChildCountResponses;
import com.tenten.linkhub.domain.space.service.dto.comment.RepliesFindResponses;
import com.tenten.linkhub.domain.space.service.dto.comment.ReplyCreateRequest;
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

    @Transactional
    public Long createReply(ReplyCreateRequest request) {
        Space space = spaceRepository.getById(request.spaceId());
        space.validateCommentAvailability();

        Comment parentComment = commentRepository.getById(request.commentId());

        Long groupNumber = parentComment.getGroupNumber();

        if (parentComment.isRootComment()) {
            groupNumber = parentComment.getId();
        }

        Comment comment = mapper.toReply(request, space, parentComment, groupNumber);

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long updateComment(CommentUpdateRequest request) {
        Space space = spaceRepository.getById(request.spaceId());
        space.validateCommentAvailability();

        Comment comment = commentRepository.getById(request.commentId());
        comment.validateCommentOwner(request.memberId());
        Comment updatedComment = comment.updateComment(request.content());

        return updatedComment.getId();
    }

    @Transactional
    public Long deleteComment(Long spaceId, Long commentId, Long memberId) {
        Space space = spaceRepository.getById(spaceId);
        space.validateCommentAvailability();

        Comment comment = commentRepository.getById(commentId);
        comment.validateCommentOwner(memberId);
        comment.delete();

        return commentId;
    }

    @Transactional(readOnly = true)
    public RepliesFindResponses findReplies(Long spaceId, Long commentId, Pageable pageable) {
        Space space = spaceRepository.getById(spaceId);
        space.validateCommentAvailability();

        Slice<Comment> responses = commentRepository.findRepliesById(commentId, pageable);

        return RepliesFindResponses.from(responses);
    }
}