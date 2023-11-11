package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkUpdateApiResponse;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.LinkUpdateFacadeRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface LinkApiMapper {

    LinkCreateFacadeRequest toLinkCreateFacadeRequest(LinkCreateApiRequest apiRequest);

    LinkCreateApiResponse toLinkCreateApiResponse(Long linkId);

    @Mapping(source = "updateLinkId", target = "linkId")
    LinkUpdateApiResponse toLinkUpdateApiResponse(Long updateLinkId);

    LinkUpdateFacadeRequest toLinkUpdateFacadeRequest(LinkUpdateApiRequest apiRequest);

}
