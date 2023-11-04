package com.tenten.linkhub.domain.member.repository;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.Provider;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<Member> findMemberWithProfileImageByMemberIds(List<Long> memberIds);

    Optional<Member> findBySocialIdAndProvider(String socialId, Provider provider);

    Member save(Member newUser);
}
