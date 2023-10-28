package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.controller.dto.SpacesFindByQueryApiRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Pageable;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceApiMapper {

    SpacesFindByQueryRequest toSpacesFindByQueryRequest(SpacesFindByQueryApiRequest request, Pageable pageable);

}
