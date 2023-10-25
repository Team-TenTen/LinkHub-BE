package com.tenten.linkhub.domain.space.model.space;

import com.tenten.linkhub.domain.space.model.category.Category;
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


@Entity
@Table(name = "spaces")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space {

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
    private boolean isVisible;

    @Column(nullable = false)
    private boolean isComment;

    @Column(nullable = false)
    private boolean isLinkSummarizable;

    @Column(nullable = false)
    private boolean isReadMarkEnabled;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Long scrapCount;

    @Column(nullable = false)
    private Long favoriteCount;

}
