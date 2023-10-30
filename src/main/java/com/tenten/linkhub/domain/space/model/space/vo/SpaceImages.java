package com.tenten.linkhub.domain.space.model.space.vo;

import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.global.exception.MaxImageCountExceededException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Getter
@Embeddable
@NoArgsConstructor
public class SpaceImages {
    private static final Integer MAX_IMAGE_COUNT = 1;

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<SpaceImage> spaceImageList = new ArrayList<>();

    public void addSpaceImage(SpaceImage spaceImage) {
        validateNotNull(spaceImage, "spaceImage");
        if (this.spaceImageList.size() >= MAX_IMAGE_COUNT ){
            throw new MaxImageCountExceededException("스페이스 이미지는 최대 1개 입니다.");
        }

        this.spaceImageList.add(spaceImage);
    }

    public void remove(SpaceImage spaceImage){
        this.spaceImageList.remove(spaceImage);
    }

}
