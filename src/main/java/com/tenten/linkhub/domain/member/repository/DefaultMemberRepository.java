package com.tenten.linkhub.domain.member.repository;

import org.springframework.stereotype.Repository;

@Repository
public class DefaultMemberRepository implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public DefaultMemberRepository(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public boolean existsMemberByNewsEmail(String email) {
        return memberJpaRepository.existsByNewsEmail(email);
    }
}
