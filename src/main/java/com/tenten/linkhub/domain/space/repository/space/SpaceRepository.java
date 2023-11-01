package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceWithSpaceImageAndSpaceMember;
import org.springframework.data.domain.Slice;

public interface SpaceRepository {

    Slice<SpaceWithSpaceImageAndSpaceMember> findSpaceWithSpaceImageByQuery(QueryCondition queryCondition);

    Space save(Space space);

    Space getById(Long spaceId);

    Space getSpaceJoinSpaceImageAndSpaceMemberById(Long spaceId);
}
