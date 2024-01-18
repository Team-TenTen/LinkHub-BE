package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImage;
import com.tenten.linkhub.domain.space.repository.space.dto.MemberSpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import org.springframework.data.domain.Slice;

public interface SpaceRepository {

    Slice<SpaceAndSpaceImage> findPublicSpacesJoinSpaceImageByQuery(QueryCondition queryCondition);

    Space save(Space space);

    Space getById(Long spaceId);

    Space getSpaceJoinSpaceMemberById(Long spaceId);

    Slice<SpaceAndSpaceImage> findMemberSpacesJoinSpaceImageByQuery(MemberSpacesQueryCondition queryCondition);

    void increaseFavoriteCount(Long spaceId);

    void decreaseFavoriteCount(Long spaceId);

    void increaseScrapCount(Long spaceId);
}
