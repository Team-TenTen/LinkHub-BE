package com.tenten.linkhub.domain.member.repository.member;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.Provider;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultMemberRepository implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public DefaultMemberRepository(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public List<Member> findMemberWithProfileImageByMemberIds(List<Long> memberIds) {
        return memberJpaRepository.findMemberJoinProfileImageByMemberIds(memberIds);

    }

    public boolean existsMemberByNewsEmail(String email) {
        return memberJpaRepository.existsByNewsEmail(email);
    }
    @Override
    public Optional<Member> findBySocialIdAndProvider(String socialId, Provider provider) {
        return memberJpaRepository.findBySocialIdAndProvider(socialId, provider);
    }

    @Override
    public Member save(Member newUser) {
        return memberJpaRepository.save(newUser);
    }

    @Override
    public Optional<Member> findByIdWithImageAndCategory(Long memberId) {
        return memberJpaRepository.findByIdWithImageAndCategory(memberId);
    }

}
