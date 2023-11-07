package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.model.space.SpaceImage;

import java.util.List;

public record DeletedSpaceImageNames(
        List<String> fileNames
) {
    public static DeletedSpaceImageNames from(List<SpaceImage> spaceImages){
        List<String> spaceImageNames = spaceImages.stream()
                .map(si -> si.getName())
                .toList();

        return new DeletedSpaceImageNames(
                spaceImageNames
        );
    }

}
