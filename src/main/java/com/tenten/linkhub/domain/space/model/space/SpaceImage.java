package com.tenten.linkhub.domain.space.model.space;

import com.tenten.linkhub.global.entity.BaseEntity;
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

import static com.tenten.linkhub.global.util.CommonValidator.validateMaxSize;
import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Entity
@Table(name = "space_images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpaceImage extends BaseEntity {
    private static final int MAX_PATH_LENGTH = 2083;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", referencedColumnName = "id")
    private Space space;

    @Column(length = MAX_PATH_LENGTH, nullable = false)
    private String path;

    @Column(nullable = false)
    private String name;

    public SpaceImage(String path, String name) {
        validateMaxSize(path, MAX_PATH_LENGTH, "path");
        validateMaxSize(name, 255, "name");

        this.path = path;
        this.name = name;
    }

    public void changeSpace(Space space) {
        if (this.space != null) {
            this.space.removeSpaceImage(this);
        }

        this.space = space;
    }

    public Long deleteSpaceImage(){
        this.isDeleted = true;
        return id;
    }

}
