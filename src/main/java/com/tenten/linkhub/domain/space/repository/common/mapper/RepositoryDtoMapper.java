package com.tenten.linkhub.domain.space.repository.common.mapper;

import com.tenten.linkhub.domain.space.common.SpaceCursorPageRequest;
import com.tenten.linkhub.domain.space.common.SpaceCursorSlice;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickName;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RepositoryDtoMapper {

    public List<SpaceAndSpaceImageOwnerNickName> toSpaceAndSpaceImageOwnerNickNames(List<SpaceAndOwnerNickName> spaceAndOwnerNickNames, List<SpaceImage> spaceImages) {
        Map<Long, List<SpaceImage>> spaceImageMap = spaceImages
                .stream()
                .collect(Collectors.groupingBy(
                        si -> si.getSpace().getId()
                ));

        return spaceAndOwnerNickNames
                .stream()
                .map(so -> new SpaceAndSpaceImageOwnerNickName(
                        so.space(),
                        spaceImageMap.get(so.space().getId()),
                        so.ownerNickName()
                ))
                .collect(Collectors.toList());
    }

    public SpaceCursorSlice<SpaceAndSpaceImageOwnerNickName> toSpaceCursorSlice(List<SpaceAndSpaceImageOwnerNickName> contents, SpaceCursorPageRequest pageable, boolean hasNext){
        Space lastSpace = contents.isEmpty() ? null : contents.get(contents.size() - 1).space();

        return SpaceCursorSlice.of(
                Objects.isNull(lastSpace) ? null : lastSpace.getFavoriteCount(),
                Objects.isNull(lastSpace) ? null : lastSpace.getId(),
                pageable.pageSize(),
                hasNext,
                contents
        );
    }

}
