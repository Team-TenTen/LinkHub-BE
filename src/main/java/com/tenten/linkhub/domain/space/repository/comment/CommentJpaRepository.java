package com.tenten.linkhub.domain.space.repository.comment;

import com.tenten.linkhub.domain.space.model.space.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.groupNumber = :commentId AND c.isDeleted = false ORDER BY c.createdAt ASC")
    Slice<Comment> findRepliesByParentCommentId(Long commentId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.id = :commentId AND c.isDeleted = false")
    Optional<Comment> findById(Long commentId);
}
