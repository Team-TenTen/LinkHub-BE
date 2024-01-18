package com.tenten.linkhub.domain.space.repository.common.dto;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record SpaceAndSpaceImages(
        List<SpaceAndSpaceImage> contents
) {
    public static SpaceAndSpaceImages of(List<Space> spaces, List<SpaceImage> spaceImages) {
        Map<Long, List<SpaceImage>> spaceImageMap = spaceImages
                .stream()
                .collect(Collectors.groupingBy(
                        si -> si.getSpace().getId()
                ));

        List<SpaceAndSpaceImage> mapSpaceAndSpaceImages = spaces
                .stream()
                .map(s -> new SpaceAndSpaceImage(
                        s,
                        spaceImageMap.get(s.getId())
                ))
                .collect(Collectors.toList());

        return new SpaceAndSpaceImages(mapSpaceAndSpaceImages);
    }
}
