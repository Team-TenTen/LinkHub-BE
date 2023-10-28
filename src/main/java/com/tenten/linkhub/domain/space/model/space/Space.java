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

}
