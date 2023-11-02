package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.model.space.dto.SpaceUpdateDto;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryRequest;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceMapper {

    QueryCondition toQueryCond(SpacesFindByQueryRequest request);

    Space toSpace(SpaceCreateRequest request);

    @Mapping(source = "request.memberId", target = "memberId")
    SpaceMember toSpaceMember(SpaceCreateRequest request, Role role);

    SpaceImage toSpaceImage(ImageInfo imageInfo);

    @Mapping(source = "request.imageInfo", target = "imageInfo", qualifiedByName = "mapSpaceImage")
    SpaceUpdateDto toSpaceUpdateDto(SpaceUpdateRequest request);

    @Named("mapSpaceImage")
    static Optional<SpaceImage> mapSpaceImage(Optional<ImageInfo> imageInfo){
        if (imageInfo.isPresent()){
            return imageInfo.map(i -> new SpaceImage(i.path(), i.name()));
        }

        return Optional.empty();
    }

}
