package com.tenten.linkhub.domain.space.model.link;

import com.tenten.linkhub.domain.space.model.space.Space;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(length = 30, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Color color;

    public static Tag toTag(Space space,
                            String name,
                            Color color) {
        return new Tag(space, name, color);
    }

    private Tag(Space space, String name, Color color) {
        this.space = space;
        this.name = name;
        this.color = color;
    }

}
