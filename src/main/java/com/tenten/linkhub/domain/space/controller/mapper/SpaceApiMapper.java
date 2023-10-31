package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.controller.dto.space.SpaceCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpacesFindByQueryApiRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceApiMapper {

    SpacesFindByQueryRequest toSpacesFindByQueryRequest(SpacesFindByQueryApiRequest request, Pageable pageable);

    SpaceCreateRequest toSpaceCreateRequest(SpaceCreateApiRequest request, MultipartFile file);

}
