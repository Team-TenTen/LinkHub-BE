package com.tenten.linkhub.domain.space.controller.mapper;

import com.tenten.linkhub.domain.space.controller.dto.invitation.SpaceInvitationApiRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceInvitationFacadeRequest;
import com.tenten.linkhub.domain.space.service.dto.invitation.SpaceInvitationAcceptRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpaceInvitationApiMapper {
    SpaceInvitationFacadeRequest toSpaceInvitationFacadeRequest(SpaceInvitationApiRequest request, Long myMemberId);

    SpaceInvitationAcceptRequest toSpaceInvitationAcceptRequest(Long notificationId, Long memberId);
}
