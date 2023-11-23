package com.tenten.linkhub.domain.space.controller.dto.spacemember;

import com.tenten.linkhub.domain.space.model.space.Role;

public record SpaceMemberRoleChangeApiRequest(
        Long targetMemberId,
        Role role
) {
}
