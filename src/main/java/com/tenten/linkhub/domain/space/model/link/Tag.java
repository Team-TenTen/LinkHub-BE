package com.tenten.linkhub.domain.space.model.link;

import com.tenten.linkhub.domain.space.model.space.Space;
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

@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", nullable = false)
    private Link link;

    @Column(nullable = false)
    private String name;

    public static Tag toTag(Space space, Link link, String name) {
        return new Tag(space, link, name);
    }

    public void changeLink(Link link) {
        this.link = link;
    }

    public void deleteTag() {
        this.link = null;
    }

    private Tag(Space space, Link link, String name) {
        this.space = space;
        this.link = link;
        this.name = name;
    }

}
