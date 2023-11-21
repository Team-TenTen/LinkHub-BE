package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.model.space.Favorite;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.favorite.dto.MyFavoriteSpacesQueryCondition;
import com.tenten.linkhub.domain.space.service.dto.favorite.MyFavoriteSpacesFindRequest;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    public Favorite toFavorite(Space space, Long memberId) {
        return new Favorite(space, memberId);
    }

    public MyFavoriteSpacesQueryCondition toQueryCondition(MyFavoriteSpacesFindRequest request) {
        return new MyFavoriteSpacesQueryCondition(
                request.pageable(),
                request.keyWord(),
                request.filter(),
                request.memberId()
        );
    }

}
