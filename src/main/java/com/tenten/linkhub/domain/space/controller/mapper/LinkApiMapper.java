package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkUpdateApiResponse;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.LinkUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.model.link.Color;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface LinkApiMapper {

    @Mapping(target = "color", source = "color", qualifiedByName = "toColor")
    LinkCreateFacadeRequest toLinkCreateFacadeRequest(LinkCreateApiRequest apiRequest);

    LinkCreateApiResponse toLinkCreateApiResponse(Long linkId);

    @Mapping(source = "updateLinkId", target = "linkId")
    LinkUpdateApiResponse toLinkUpdateApiResponse(Long updateLinkId);

    @Mapping(target = "color", source = "color", qualifiedByName = "toColor")
    LinkUpdateFacadeRequest toLinkUpdateFacadeRequest(LinkUpdateApiRequest apiRequest);

    @Named("toColor")
    static Color toColor(String tag) {
        for (Color color : Color.values()) {
            if (color.getValue().equals(tag)) {
                return color;
            }
        }
        throw new IllegalArgumentException("정의된 태그의 색이 아닙니다.");
    }
}
