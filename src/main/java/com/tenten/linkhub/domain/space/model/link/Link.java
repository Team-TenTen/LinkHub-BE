package com.tenten.linkhub.domain.space.model.link;

import com.tenten.linkhub.domain.space.model.link.vo.Url;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Entity
@Table(name = "links")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Link extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @OneToMany(mappedBy = "link", cascade = CascadeType.PERSIST)
    private List<LinkTag> linkTags = new ArrayList<>();

    @Column(nullable = false)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String title;

    @Embedded
    private Url url;

    @Column(nullable = false)
    private Long likeCount;

    @Version
    private int version;

    @OneToMany(mappedBy = "link", cascade = CascadeType.PERSIST)
    private List<LinkViewHistory> linkViewHistories = new ArrayList<>();

    public void addLinkTag(LinkTag linkTag) {
        linkTags.add(linkTag);
        linkTag.changeLink(this);
    }

    public static Link toLink(Space space,
                              Long memberId,
                              String title,
                              Url url) {
        return new Link(
                space,
                memberId,
                title,
                url);
    }

    private Link(Space space,
                 Long memberId,
                 String title,
                 Url url) {
        validateNotNull(space, "space");
        validateNotNull(memberId, "memberId");
        validateNotNull(title, "title");
        validateNotNull(url, "url");

        this.space = space;
        this.memberId = memberId;
        this.title = title;
        this.url = url;
        this.likeCount = 0L;
    }

    public void updateLink(Url url, String title, LinkTag linkTag) {
        validateNotNull(url, "url");
        validateNotNull(title, "title");
        this.url = url;
        this.title = title;

        if (hasLinkTag()) { //기존에 태그를 사용하는 링크였다면 삭제 처리
            deleteLinkTag();
        }

        this.linkTags.add(linkTag);
    }

    public void updateLink(Url url, String title) {
        validateNotNull(url, "url");
        validateNotNull(title, "title");
        this.url = url;
        this.title = title;

        if (hasLinkTag()) { //기존에 태그를 사용하는 링크였다면 삭제 처리
            deleteLinkTag();
        }
    }

    private boolean hasLinkTag() {
        return !linkTags.isEmpty();
    }

    private void deleteLinkTag() {
        this.linkTags.forEach(LinkTag::deleteLink);
        this.linkTags.clear();
    }

    public void increaseLikeCount() {
        likeCount++;
    }

    public void decreaseLikeCount() {
        likeCount--;
    }

    public void deleteLink() {
        this.isDeleted = true;
        deleteLinkTag();
    }
}
