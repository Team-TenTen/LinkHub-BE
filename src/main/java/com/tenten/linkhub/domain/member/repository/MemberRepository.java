package com.tenten.linkhub.domain.member.repository;

import com.tenten.linkhub.domain.member.model.Member;

import java.util.List;

public interface MemberRepository {
    List<Member> findMemberWithProfileImageByMemberIds(List<String> memberIds);
}
