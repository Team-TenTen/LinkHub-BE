package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.repository.space.dto.QueryCond;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceMapper {

    QueryCond toQueryCond(SpacesFindByQueryRequest request);

}
