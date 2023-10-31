package com.tenten.linkhub.domain.space.repository.spaceimage.dto;

import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;

public record SpaceMemberWithMemberInfo(
        SpaceMember,
        String memberNickname,
        String memberAboutMe,
        Role
        String memberImagePath,
) {
}
