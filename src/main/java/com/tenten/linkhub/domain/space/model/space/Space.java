package com.tenten.linkhub.domain.space.model.space;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.dto.SpaceUpdateDto;
import com.tenten.linkhub.domain.space.model.space.vo.SpaceImages;
import com.tenten.linkhub.domain.space.model.space.vo.SpaceMembers;
import com.tenten.linkhub.global.entity.BaseEntity;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(length = 500)
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

    public void changeSpaceImage(SpaceImage spaceImage) {
        this.spaceImages.changeSpaceImage(spaceImage);
    }

    /**
     * Space와 SpaceImage간의 편의 메서드용 메서드.
     */
    public void removeSpaceImage(SpaceImage spaceImage) {
        spaceImages.removeSpaceImage(spaceImage);
    }

    /**
     * Space와 SpaceMember간의 편의 메서드용 메서드.
     */
    public void removeSpaceMember(SpaceMember spaceMember) {
        spaceMembers.removeSpaceMember(spaceMember);
    }

    public void increaseViewCount() {
        viewCount++;
    }

    public Boolean isOwner(Long memberId) {
        return Objects.equals(this.memberId, memberId);
    }

    public void validateOwnership(Long memberId) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new UnauthorizedAccessException("해당 멤버는 이 스페이스의 owner가 아닙니다.");
        }
    }

    public void validateCommentAvailability() {
        if (!this.isComment) {
            throw new UnauthorizedAccessException("해당 게시글은 댓글을 작성할 수 없습니다.");
        }
    }

    public void updateSpaceAttributes(SpaceUpdateDto updateDto) {
        validateOwnership(updateDto.memberId());

        validateMaxSize(updateDto.spaceName(), 255, "spaceName");
        validateNotNull(updateDto.category(), "category");
        validateNotNull(updateDto.isVisible(), "isVisible");
        validateNotNull(updateDto.isComment(), "isComment");
        validateNotNull(updateDto.isLinkSummarizable(), "isLinkSummarizable");
        validateNotNull(updateDto.isReadMarkEnabled(), "isReadMarkEnabled");

        this.spaceName = updateDto.spaceName();
        this.description = updateDto.description();
        this.category = updateDto.category();
        this.isVisible = updateDto.isVisible();
        this.isComment = updateDto.isComment();
        this.isLinkSummarizable = updateDto.isLinkSummarizable();
        this.isReadMarkEnabled = updateDto.isReadMarkEnabled();
        updateDto.spaceImage()
                .ifPresent(this::changeSpaceImage);
    }

    public Long deleteSpace(Long memberId) {
        validateOwnership(memberId);

        this.spaceMembers.deleteAll();
        this.spaceImages.deleteAll();
        this.isDeleted = true;

        return id;
    }

    public List<SpaceImage> getAllSpaceImages() {
        return spaceImages.getAllSpaceImages();
    }

}
