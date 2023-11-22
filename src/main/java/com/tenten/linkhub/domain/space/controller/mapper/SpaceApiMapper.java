package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.member.controller.dto.MemberSpacesFindApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.PublicSpacesFindWithFilterApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceTagsGetApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.PublicSpacesFindByQueryApiRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponses;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryRequest;
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

    MemberSpacesFindRequest toMySpacesFindRequest(Pageable pageable, MemberSpacesFindApiRequest request, Long memberId);

    SpaceTagsGetApiResponse toSpaceTagsGetApiResponse(SpaceTagGetResponses response);
}
