package com.tenten.linkhub.domain.space.model.link;

import com.tenten.linkhub.global.entity.BaseEntity;
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

@Entity
@Table(name = "link_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LinkTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", nullable = false)
    private Link link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public void changeLink(Link link) {
        this.link = link;
    }

    public static LinkTag toLinkTag(Link link, Tag tag) {
        return new LinkTag(link, tag);
    }

    private LinkTag(Link link, Tag tag) {
        this.link = link;
        this.tag = tag;
    }
}
