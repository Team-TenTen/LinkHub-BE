package com.tenten.linkhub.domain.space.controller.dto.spacemember;

public record SpaceMemberRoleChangeApiResponse(
        Long spaceId
) {
    public static SpaceMemberRoleChangeApiResponse from(Long spaceId) {
        return new SpaceMemberRoleChangeApiResponse(spaceId);
    }
}
