package com.tenten.linkhub.domain.space.service.dto.space;

import com.tenten.linkhub.domain.space.model.space.Role;

public record SpaceMemberInfo(
        Long memberId,
        Role role
) {
}
