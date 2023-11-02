package com.tenten.linkhub.domain.space.facade.dto;

import com.tenten.linkhub.domain.space.model.space.Role;

public record SpaceMemberDetailInfo(
        Long memberId,
        String nickname,
        String aboutMe,
        String profilePath,
        Role SpaceMemberRole
) {
}
