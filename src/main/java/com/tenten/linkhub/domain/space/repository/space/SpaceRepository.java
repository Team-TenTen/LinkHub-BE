package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickName;
import com.tenten.linkhub.domain.space.repository.space.dto.MemberSpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import org.springframework.data.domain.Slice;

public interface SpaceRepository {

    Slice<SpaceAndSpaceImageOwnerNickName> findPublicSpacesJoinSpaceImageByQuery(QueryCondition queryCondition);

    Space save(Space space);

    Space getById(Long spaceId);

    Space getSpaceJoinSpaceMemberById(Long spaceId);

    Slice<SpaceAndSpaceImageOwnerNickName> findMemberSpacesJoinSpaceImageByQuery(MemberSpacesQueryCondition queryCondition);

    void increaseFavoriteCount(Long spaceId);

    void decreaseFavoriteCount(Long spaceId);

    void increaseScrapCount(Long spaceId);

    Slice<SpaceAndSpaceImageOwnerNickName> findPublicSpacesJoinSpaceImageByLikeQuery(QueryCondition queryCondition);
}
