package com.tenten.linkhub.domain.space.service.dto.spacemember;

import com.tenten.linkhub.domain.space.model.space.dto.SpaceMemberRole;

import java.util.List;

public record SpaceMemberRoleChangeRequest(
        Long spaceId,
        Long memberId,
        List<SpaceMemberRole> spaceMemberRoles
) {
}
