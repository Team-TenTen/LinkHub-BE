package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.controller.dto.MySpacesFindApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpacesFindByQueryApiRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.service.dto.space.MySpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryRequest;
import jakarta.servlet.http.Cookie;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceApiMapper {

    SpacesFindByQueryRequest toSpacesFindByQueryRequest(SpacesFindByQueryApiRequest request, Pageable pageable);

    SpaceCreateFacadeRequest toSpaceCreateFacadeRequest(SpaceCreateApiRequest request, MultipartFile file, Long memberId);

    SpaceUpdateFacadeRequest toSpaceUpdateFacadeRequest(Long spaceId, SpaceUpdateApiRequest request, MultipartFile file, Long memberId);

    SpaceDetailGetByIdFacadeRequest toSpaceDetailGetByIdFacadeRequest(Long spaceId, Long memberId, List<Long> spaceViews);

    MySpacesFindRequest toMySpacesFindRequest(Pageable pageable, MySpacesFindApiRequest request, Long memberId);
}
