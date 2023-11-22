package com.tenten.linkhub.domain.space.controller.dto.spacemember;

import com.tenten.linkhub.domain.space.model.space.dto.SpaceMemberRole;

import java.util.List;

public record SpaceMemberRoleChangeApiRequest(
        List<SpaceMemberRole> spaceMemberRoles
) {
}
