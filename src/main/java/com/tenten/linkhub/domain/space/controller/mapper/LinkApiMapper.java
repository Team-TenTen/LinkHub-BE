package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiResponse;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface LinkApiMapper {

    LinkCreateFacadeRequest toLinkCreateFacadeRequest(LinkCreateApiRequest apiRequest);

    LinkCreateApiResponse toLinkCreateApiResponse(Long linkId);

}
