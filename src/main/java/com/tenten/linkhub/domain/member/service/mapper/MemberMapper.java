package com.tenten.linkhub.domain.member.service.mapper;

import com.tenten.linkhub.domain.member.repository.dto.MemberSearchQueryCondition;
import com.tenten.linkhub.domain.member.service.dto.MemberSearchRequest;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberSearchQueryCondition toQueryCond(MemberSearchRequest request) {
        return new MemberSearchQueryCondition(
                request.keyword(),
                request.pageable(),
                request.myMemberId());
    }

}
