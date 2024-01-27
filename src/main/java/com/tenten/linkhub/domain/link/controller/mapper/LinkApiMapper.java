package com.tenten.linkhub.domain.link.controller.mapper;

import com.tenten.linkhub.domain.link.controller.dto.LinkCreateApiRequest;
import com.tenten.linkhub.domain.link.controller.dto.LinkCreateApiResponse;
import com.tenten.linkhub.domain.link.controller.dto.LinkUpdateApiRequest;
import com.tenten.linkhub.domain.link.controller.dto.LinkUpdateApiResponse;
import com.tenten.linkhub.domain.link.controller.dto.LinksGetWithFilterApiRequest;
import com.tenten.linkhub.domain.link.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.link.facade.dto.LinkUpdateFacadeRequest;
import com.tenten.linkhub.domain.link.service.dto.LinksGetByQueryRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Pageable;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface LinkApiMapper {

    LinkCreateFacadeRequest toLinkCreateFacadeRequest(LinkCreateApiRequest apiRequest);

    LinkCreateApiResponse toLinkCreateApiResponse(Long linkId);

    @Mapping(source = "updateLinkId", target = "linkId")
    LinkUpdateApiResponse toLinkUpdateApiResponse(Long updateLinkId);

    LinkUpdateFacadeRequest toLinkUpdateFacadeRequest(LinkUpdateApiRequest apiRequest);

    LinksGetByQueryRequest toLinksGetByQueryRequest(LinksGetWithFilterApiRequest request, Pageable pageable, Long spaceId, Long memberId);
}
