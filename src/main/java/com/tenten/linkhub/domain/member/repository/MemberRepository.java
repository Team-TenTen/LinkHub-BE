package com.tenten.linkhub.domain.member.repository;

import com.tenten.linkhub.domain.member.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<Member> findMemberWithProfileImageByMemberIds(List<Long> memberIds);

    <T> Optional<T> findBySocialIdAndProvider(String socialId, String provider);
}
