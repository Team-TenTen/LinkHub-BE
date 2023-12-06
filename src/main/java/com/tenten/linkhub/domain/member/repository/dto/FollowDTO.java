package com.tenten.linkhub.domain.member.repository.dto;

import com.tenten.linkhub.domain.member.model.Follow;

public record FollowDTO(
        Follow follow,
        Boolean isFollowing) {
}
