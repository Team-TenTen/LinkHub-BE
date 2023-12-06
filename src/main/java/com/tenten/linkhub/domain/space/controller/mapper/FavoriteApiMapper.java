package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.controller.dto.favorite.MyFavoriteSpacesFindApiRequest;
import com.tenten.linkhub.domain.space.service.dto.favorite.MyFavoriteSpacesFindRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface FavoriteApiMapper {
    MyFavoriteSpacesFindRequest toMyFavoriteSpacesFindRequest(Pageable pageable, MyFavoriteSpacesFindApiRequest request, Long memberId);
}
