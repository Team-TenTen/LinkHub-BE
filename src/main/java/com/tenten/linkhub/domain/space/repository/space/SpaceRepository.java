package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceWithSpaceImage;
import org.springframework.data.domain.Slice;

public interface SpaceRepository {

    Slice<SpaceWithSpaceImage> findSpaceWithSpaceImageByQuery(QueryCondition queryCondition);

    Space save(Space space);

    Space getById(Long spaceId);

    SpaceWithSpaceImage getSpaceWithSpaceImageById(Long spaceId);
}
