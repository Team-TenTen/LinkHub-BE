package com.tenten.linkhub.domain.space.repository.common.dto;

import com.tenten.linkhub.domain.space.model.space.SpaceImage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record SpaceAndSpaceImageOwnerNickNames(
        List<SpaceAndSpaceImageOwnerNickName> contents
) {
    public static SpaceAndSpaceImageOwnerNickNames of(List<SpaceAndOwnerNickName> spaceAndOwnerNickNames, List<SpaceImage> spaceImages) {
        Map<Long, List<SpaceImage>> spaceImageMap = spaceImages
                .stream()
                .collect(Collectors.groupingBy(
                        si -> si.getSpace().getId()
                ));

        List<SpaceAndSpaceImageOwnerNickName> mapSpaceAndSpaceImageOwnerNickNames = spaceAndOwnerNickNames
                .stream()
                .map(so -> new SpaceAndSpaceImageOwnerNickName(
                        so.space(),
                        spaceImageMap.get(so.space().getId()),
                        so.ownerNickName()
                ))
                .collect(Collectors.toList());

        return new SpaceAndSpaceImageOwnerNickNames(mapSpaceAndSpaceImageOwnerNickNames);
    }
}
