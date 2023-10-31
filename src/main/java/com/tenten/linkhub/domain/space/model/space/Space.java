package com.tenten.linkhub.domain.space.model.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.vo.SpaceImages;
import com.tenten.linkhub.domain.space.model.space.vo.SpaceMembers;
import com.tenten.linkhub.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

import java.util.List;

import static com.tenten.linkhub.global.util.CommonValidator.validateMaxSize;
import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

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

    @Embedded
    private SpaceImages spaceImages = new SpaceImages();

    @Embedded
    private SpaceMembers spaceMembers = new SpaceMembers();

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
        validateNotNull(memberId, "memberId");
        validateNotNull(category, "category");
        validateNotNull(isVisible, "isVisible");
        validateNotNull(isComment, "isComment");
        validateNotNull(isLinkSummarizable, "isLinkSummarizable");
        validateNotNull(isReadMarkEnabled, "isReadMarkEnabled");
        validateMaxSize(spaceName, 255, "spaceName");

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

    public void addSpaceImage(SpaceImage spaceImage) {
        this.spaceImages.addSpaceImage(spaceImage);
        spaceImage.changeSpace(this);
    }

    public void addSpaceMember(SpaceMember spaceMember) {
        this.spaceMembers.addSpaceMember(spaceMember);
        spaceMember.changeSpace(this);
    }

    public List<SpaceImage> getSpaceImages() {
        return spaceImages.getSpaceImageList();
    }

    public List<SpaceMember> getSpaceMembers() {
        return spaceMembers.getSpaceMemberList();
    }

    public void removeSpaceImage(SpaceImage spaceImage){
        spaceImages.removeSpaceImage(spaceImage);
    }

    public void removeSpaceMember(SpaceMember spaceMember){
        spaceMembers.removeSpaceMember(spaceMember);
    }

}
