package com.tenten.linkhub.domain.member.repository.dto;

import org.springframework.data.domain.Pageable;

public record MemberSearchQueryCondition(
        String keyword,
        Pageable pageable,
        Long myMemberId
) {

}
