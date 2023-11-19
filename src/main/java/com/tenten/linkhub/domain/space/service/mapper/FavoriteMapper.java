package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.model.space.Favorite;
import com.tenten.linkhub.domain.space.model.space.Space;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    public Favorite toFavorite(Space space, Long memberId) {
        return new Favorite(space, memberId);
    }
}
