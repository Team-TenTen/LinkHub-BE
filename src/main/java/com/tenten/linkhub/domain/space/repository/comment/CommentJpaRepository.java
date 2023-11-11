package com.tenten.linkhub.domain.space.repository.comment;

import com.tenten.linkhub.domain.space.model.space.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
