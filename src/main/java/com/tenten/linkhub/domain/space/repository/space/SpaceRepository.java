package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.MySpacesFindQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceAndOwnerNickName;
import org.springframework.data.domain.Slice;

public interface SpaceRepository {

    Slice<SpaceAndOwnerNickName> findPublicSpacesJoinSpaceImageByQuery(QueryCondition queryCondition);

    Space save(Space space);

    Space getById(Long spaceId);

    Space getSpaceJoinSpaceMemberById(Long spaceId);

    Slice<SpaceAndOwnerNickName> findMySpacesJoinSpaceImageByQuery(MySpacesFindQueryCondition mySpacesFindQueryCondition);

    void increaseFavoriteCount(Long spaceId);

    void decreaseFavoriteCount(Long spaceId);

}
