package com.tenten.linkhub.domain.link.facade.mapper;

import com.tenten.linkhub.domain.link.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.link.facade.dto.LinkUpdateFacadeRequest;
import com.tenten.linkhub.domain.link.service.dto.LinkCreateRequest;
import com.tenten.linkhub.domain.link.service.dto.LinkUpdateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface LinkFacadeMapper {
    @Mapping(source = "spaceId", target = "spaceId")
    @Mapping(source = "memberId", target = "memberId")
    LinkCreateRequest toLinkCreateRequest(LinkCreateFacadeRequest request, Long memberId, Long spaceId);

    @Mapping(source = "spaceId", target = "spaceId")
    @Mapping(source = "memberId", target = "memberId")
    @Mapping(source = "linkId", target = "linkId")
    LinkUpdateRequest toLinkUpdateRequest(LinkUpdateFacadeRequest request, Long memberId, Long spaceId, Long linkId);
}
