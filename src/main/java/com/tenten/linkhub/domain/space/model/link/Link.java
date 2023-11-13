package com.tenten.linkhub.domain.space.model.link;

import com.tenten.linkhub.domain.space.model.link.vo.Url;
import com.tenten.linkhub.domain.space.model.space.Space;
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
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @OneToMany(mappedBy = "link", cascade = CascadeType.PERSIST)
    private List<Tag> tags = new ArrayList<>();

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String title;

    @Embedded
    private Url url;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Long likeCount;

    @Version
    private int version;

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.changeLink(this);
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
        this.viewCount = 0L;
    }

    public void increaseLikeCount() {
        likeCount++;
    }

    public void decreaseLikeCount() {
        likeCount--;
    }
}
