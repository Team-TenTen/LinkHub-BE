package com.tenten.linkhub.domain.space.model.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Table(name = "spaces")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String spaceName;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Boolean isVisible;

    @Column(nullable = false)
    private Boolean isComment;

    @Column(nullable = false)
    private Boolean isLinkSummarizable;

    @Column(nullable = false)
    private Boolean isReadMarkEnabled;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Long scrapCount;

    @Column(nullable = false)
    private Long favoriteCount;

    public Space(Long memberId, String spaceName, String description, Category category, Boolean isVisible, Boolean isComment, Boolean isLinkSummarizable, Boolean isReadMarkEnabled) {
        Assert.notNull(memberId, "memberId는 null이 될 수 없습니다.");
        Assert.notNull(spaceName, "spaceName은 null이 될 수 없습니다.");
        Assert.notNull(category, "category는 null이 될 수 없습니다.");
        Assert.notNull(isVisible, "isVisible는 null이 될 수 없습니다.");
        Assert.notNull(isComment, "isComment는 null이 될 수 없습니다.");
        Assert.notNull(isLinkSummarizable, "isLinkSummarizable는 null이 될 수 없습니다.");
        Assert.notNull(isReadMarkEnabled, "isReadMarkEnabled는 null이 될 수 없습니다.");

        this.memberId = memberId;
        this.spaceName = spaceName;
        this.description = description;
        this.category = category;
        this.isVisible = isVisible;
        this.isComment = isComment;
        this.isLinkSummarizable = isLinkSummarizable;
        this.isReadMarkEnabled = isReadMarkEnabled;
        this.viewCount = 0L;
        this.scrapCount = 0L;
        this.favoriteCount = 0L;
    }

}
