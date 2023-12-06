package com.tenten.linkhub.domain.member.service.dto;

import org.springframework.data.domain.Pageable;

public record MemberSearchRequest(
        String keyword,
        Pageable pageable,
        Long myMemberId
) {

}
