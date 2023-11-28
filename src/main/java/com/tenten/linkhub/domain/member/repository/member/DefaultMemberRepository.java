package com.tenten.linkhub.domain.member.repository.member;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.dto.MemberSearchQueryCondition;
import com.tenten.linkhub.domain.member.repository.dto.MemberWithProfileImageAndFollowingStatus;
import com.tenten.linkhub.domain.member.repository.query.MemberQueryRepository;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DefaultMemberRepository implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberQueryRepository memberQueryRepository;

    public DefaultMemberRepository(MemberJpaRepository memberJpaRepository,
            MemberQueryRepository memberQueryRepository) {
        this.memberJpaRepository = memberJpaRepository;
        this.memberQueryRepository = memberQueryRepository;
    }

    @Override
    public List<Member> findMemberWithProfileImageByMemberIds(List<Long> memberIds) {
        return memberJpaRepository.findMemberJoinProfileImageByMemberIds(memberIds);

    }

    public boolean existsMemberByNewsEmail(String email) {
        return memberJpaRepository.existsByNewsEmail(email);
    }

    @Override
    public boolean existsMemberByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
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

    @Override
    public Member getById(Long memberId) {
        return memberJpaRepository.findExistingMemberById(memberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 유저입니다."));
    }

    @Override
    public List<Member> findMembersWithProfileImageAndCategoryByIds(List<Long> memberIds) {
        return memberJpaRepository.findMembersWithProfileImageAndCategoryByMemberIds(memberIds);
    }

    @Override
    public Slice<MemberWithProfileImageAndFollowingStatus> searchMember(MemberSearchQueryCondition queryCond) {
        return memberQueryRepository.findMembersWithProfileImages(queryCond);
    }

    @Override
    public Long findMemberIdByEmail(String email) {
        return memberJpaRepository.findMemberIdByEmail(email);
    }

}
