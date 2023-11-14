package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.comment.CommentRepository;
import com.tenten.linkhub.domain.space.repository.comment.dto.CommentAndChildCommentCount;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.service.dto.CommentUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentAndChildCountResponses;
import com.tenten.linkhub.domain.space.service.dto.comment.RepliesFindResponses;
import com.tenten.linkhub.domain.space.service.dto.comment.ReplyCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.RootCommentCreateRequest;
import com.tenten.linkhub.domain.space.service.mapper.CommentMapper;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

        Comment parentComment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new DataNotFoundException("부모 댓글을 찾을 수 없습니다."));

        Long groupNumber = parentComment.getGroupNumber();

        if (groupNumber == null) {
            groupNumber = parentComment.getId();
        }

        Comment comment = mapper.toReply(request, space, parentComment, groupNumber);

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long updateComment(CommentUpdateRequest request) {
        Space space = spaceRepository.getById(request.spaceId());

        space.validateCommentAvailability();

        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new DataNotFoundException("수정할 댓글을 찾을 수 없습니다."));

        if (!Objects.equals(comment.getMemberId(), request.memberId())) {
            throw new UnauthorizedAccessException("댓글을 수정 할 권한이 없습니다.");
        }

        Comment updatedComment = comment.updateComment(request.content());

        return updatedComment.getId();
    }

    public Long deleteComment(Long spaceId, Long commentId, Long memberId) {
        Space space = spaceRepository.getById(spaceId);

        space.validateCommentAvailability();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("수정할 댓글을 찾을 수 없습니다."));

        if (!Objects.equals(comment.getMemberId(), memberId)) {
            throw new UnauthorizedAccessException("댓글을 삭제 할 권한이 없습니다.");
        }

        commentRepository.deleteById(commentId);

        return commentId;
    }

    public RepliesFindResponses findReplies(Long spaceId, Long commentId, Pageable pageable) {
        Space space = spaceRepository.getById(spaceId);

        space.validateCommentAvailability();

        Slice<Comment> responses = commentRepository.findRepliesById(commentId, pageable);

        return RepliesFindResponses.from(responses);
    }
}