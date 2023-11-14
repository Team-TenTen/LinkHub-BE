
package com.tenten.linkhub.domain.space.model.space;

import com.tenten.linkhub.global.entity.BaseEntity;
import com.tenten.linkhub.global.util.CommonValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.tenten.linkhub.global.util.CommonValidator.validateMaxSize;
import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column
    private Long groupNumber;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    public Comment(Comment parentComment, Long groupNumber, String content, Long memberId, Space space) {
        validateMaxSize(content, 1000, "content");
        validateNotNull(memberId, "memberId");
        validateNotNull(space, "space");

        this.parentComment = parentComment;
        this.groupNumber = groupNumber;
        this.content = content;
        this.memberId = memberId;
        this.space = space;
    }

    public Comment updateComment(String content) {
        validateMaxSize(content, 1000, "content");

        this.content = content;

        return this;
    }
}
