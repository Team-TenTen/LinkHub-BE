package com.tenten.linkhub.domain.space.repository.favorite;

import com.tenten.linkhub.domain.space.model.space.Favorite;

public interface FavoriteRepository {

    Boolean isExist(Long memberId, Long spaceId);

    Favorite save(Favorite favorite);

    Favorite getBySpaceIdAndMemberId(Long spaceId, Long memberId);

    Long deleteById(Long favoriteId);
}
