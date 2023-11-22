package com.tenten.linkhub.domain.space.model.space.dto;

import com.tenten.linkhub.domain.space.model.space.Role;

public record SpaceMemberRole(
        Long memberId,
        Role role
) {
}
