package com.tenten.linkhub.domain.space.facade.mapper;

import com.tenten.linkhub.domain.space.facade.dto.NewSpacesScrapFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceUpdateRequest;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceFacadeMapper {

    SpaceCreateRequest toSpaceCreateRequest(SpaceCreateFacadeRequest request, ImageInfo imageInfo);

    SpaceUpdateRequest toSpaceUpdateRequest(SpaceUpdateFacadeRequest request, Optional<ImageInfo> imageInfo);

    SpaceCreateFacadeRequest toSpaceCreateFacadeRequest(NewSpacesScrapFacadeRequest request);
}
