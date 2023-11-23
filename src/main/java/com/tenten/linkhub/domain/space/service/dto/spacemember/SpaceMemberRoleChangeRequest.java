package com.tenten.linkhub.domain.space.service.dto.spacemember;

import com.tenten.linkhub.domain.space.model.space.Role;

public record SpaceMemberRoleChangeRequest(
        Long spaceId,
        Long myMemberId,
        Long targetMemberId,
        Role role
) {
}
