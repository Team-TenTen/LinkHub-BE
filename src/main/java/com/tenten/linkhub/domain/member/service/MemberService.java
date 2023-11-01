package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.member.service.dto.MemberInfos;

import java.util.List;

public interface MemberService {

    MemberInfos findMemberInfosByMemberIds(List<Long> memberIds);

}
