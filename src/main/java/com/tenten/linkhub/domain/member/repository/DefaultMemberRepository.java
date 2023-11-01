package com.tenten.linkhub.domain.member.repository;

import com.tenten.linkhub.domain.member.model.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DefaultMemberRepository implements MemberRepository{

    private final MemberJpaRepository memberJpaRepository;

    public DefaultMemberRepository(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public List<Member> findMemberWithProfileImageByMemberIds(List<Long> memberIds) {
        return memberJpaRepository.findMemberJoinProfileImageByMemberIds(memberIds);

    }

}
