package com.tenten.linkhub.domain.space.model.space;

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
import org.springframework.util.Assert;

@Entity
@Table(name = "space_images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpaceImage {
    private static final int MAX_PATH_LENGTH = 2083;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", referencedColumnName = "id", nullable = false)
    private Space space;

    @Column(length = MAX_PATH_LENGTH, nullable = false)
    private String path;

    @Column(nullable = false)
    private String name;

    public SpaceImage(Space space, String path, String name) {
        Assert.notNull(space, "space는 null이 될 수 없습니다.");
        Assert.notNull(path, "path는 null이 될 수 없습니다.");
        Assert.notNull(name, "name은 null이 될 수 없습니다.");

        this.space = space;
        this.path = path;
        this.name = name;
    }

}
