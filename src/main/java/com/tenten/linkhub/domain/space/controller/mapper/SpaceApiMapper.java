package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.controller.dto.space.MySpacesFindApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.PublicSpacesFindWithFilterApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceTagsGetApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.PublicSpacesFindByQueryApiRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.service.dto.space.MySpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagsGetResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceApiMapper {

    PublicSpacesFindByQueryRequest toPublicSpacesFindByQueryRequest(PublicSpacesFindByQueryApiRequest request, Pageable pageable);

    PublicSpacesFindByQueryRequest toPublicSpacesFindByQueryRequest(PublicSpacesFindWithFilterApiRequest request, Pageable pageable);

    SpaceCreateFacadeRequest toSpaceCreateFacadeRequest(SpaceCreateApiRequest request, MultipartFile file, Long memberId);

    SpaceUpdateFacadeRequest toSpaceUpdateFacadeRequest(Long spaceId, SpaceUpdateApiRequest request, MultipartFile file, Long memberId);

    SpaceDetailGetByIdFacadeRequest toSpaceDetailGetByIdFacadeRequest(Long spaceId, Long memberId, List<Long> spaceViews);

    MySpacesFindRequest toMySpacesFindRequest(Pageable pageable, MySpacesFindApiRequest request, Long memberId);

    SpaceTagsGetApiResponse toSpaceTagsGetApiResponse(SpaceTagsGetResponse response);
}
