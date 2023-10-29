package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.service.dto.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceMapper {

    QueryCondition toQueryCond(SpacesFindByQueryRequest request);

    Space toSpace(SpaceCreateRequest request);

    SpaceMember toSpaceMember(SpaceCreateRequest request, Role role);

}
