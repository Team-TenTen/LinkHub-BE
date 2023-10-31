package com.tenten.linkhub.domain.member.service.dto;

public record MemberInfo(
        Long memberId,
        String nickname,
        String aboutMe,
        String path
) {
}
