package com.tenten.linkhub.domain.space.repository.comment;

import com.tenten.linkhub.domain.space.model.space.Comment;

public interface CommentRepository {
    Comment save(Comment comment);
}
