package com.tenten.linkhub.domain.member.repository.member;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.Provider;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    List<Member> findMemberWithProfileImageByMemberIds(List<Long> memberIds);

    boolean existsMemberByNewsEmail(String email);

    Optional<Member> findBySocialIdAndProvider(String socialId, Provider provider);

    Member save(Member newUser);

    Optional<Member> findByIdWithImageAndCategory(Long memberId);

}
