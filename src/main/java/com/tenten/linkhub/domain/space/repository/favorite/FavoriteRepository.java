package com.tenten.linkhub.domain.space.repository.favorite;

import com.tenten.linkhub.domain.space.model.space.Favorite;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndOwnerNickName;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickName;
import com.tenten.linkhub.domain.space.repository.favorite.dto.MyFavoriteSpacesQueryCondition;
import org.springframework.data.domain.Slice;

public interface FavoriteRepository {

    Boolean isExist(Long memberId, Long spaceId);

    Favorite save(Favorite favorite);

    Favorite getBySpaceIdAndMemberId(Long spaceId, Long memberId);

    Long deleteById(Long favoriteId);

    Slice<SpaceAndSpaceImageOwnerNickName> findMyFavoriteSpacesByQuery(MyFavoriteSpacesQueryCondition queryCondition);
}
