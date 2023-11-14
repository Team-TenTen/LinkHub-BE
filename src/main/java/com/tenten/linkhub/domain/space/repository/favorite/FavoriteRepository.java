package com.tenten.linkhub.domain.space.repository.favorite;

public interface FavoriteRepository {

    Boolean isExist(Long memberId, Long spaceId);
}
