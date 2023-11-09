package com.tenten.linkhub.domain.space.model.space.vo;

import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Getter
@Embeddable
@NoArgsConstructor
public class SpaceImages {

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<SpaceImage> spaceImageList = new ArrayList<>();

    public void addSpaceImage(SpaceImage spaceImage) {
        validateNotNull(spaceImage, "spaceImage");

        this.spaceImageList.add(spaceImage);
    }

    public void changeSpaceImage(SpaceImage spaceImage) {
        validateNotNull(spaceImage, "spaceImage");

        List<SpaceImage> imageList = getSpaceImageList();

        if (imageList.isEmpty()) {
            addSpaceImage(spaceImage);
            return;
        }

        imageList.get(0).deleteSpaceImage();
        addSpaceImage(spaceImage);
    }

    /**
     * Space와 SpaceImage간의 편의 메서드용 메서드
     */
    public void removeSpaceImage(SpaceImage spaceImage){
        this.spaceImageList.remove(spaceImage);
    }

    public List<SpaceImage> getSpaceImageList() {
        return spaceImageList.stream()
                .filter(spaceImage -> spaceImage.getIsDeleted() == false)
                .collect(Collectors.toList());
    }

    public void deleteAll() {
        spaceImageList.forEach(spaceImage -> spaceImage.deleteSpaceImage());
    }

    public List<SpaceImage> getAllSpaceImages() {
        return spaceImageList.stream()
                .toList();
    }

}
