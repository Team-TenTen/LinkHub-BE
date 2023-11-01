package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.repository.MemberRepository;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberInfos findMemberInfosByMemberIds(List<Long> memberIds){
        List<Member> members = memberRepository.findMemberWithProfileImageByMemberIds(memberIds);

        return MemberInfos.from(members);
    }

}
