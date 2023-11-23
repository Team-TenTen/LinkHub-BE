package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.service.dto.MailSendResponse;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFindResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowCreateResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowersFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowingsFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberMyProfileResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberProfileResponse;
import com.tenten.linkhub.global.util.email.EmailDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MemberService {

    MailSendResponse sendVerificationEmail(EmailDto emailDto);

    MailVerificationResponse verificateEmail(MailVerificationRequest request);

    MemberFindResponse findMember(String socialId, Provider provider);

    MemberInfos findMemberInfosByMemberIds(List<Long> memberIds);

    MemberJoinResponse join(MemberJoinRequest memberJoinRequest);

    MemberProfileResponse getProfile(Long memberId, Long myMemberId);

    MemberMyProfileResponse getMyProfile(Long memberId);

    MemberFollowCreateResponse createFollow(Long memberId, Long myMemberId);

    Long deleteFollow(Long memberId, Long myMemberId);

    MemberFollowingsFindResponses getFollowings(Long memberId, Long myMemberId, PageRequest pageRequest);

    MemberFollowersFindResponses getFollowers(Long memberId, Long myMemberId, PageRequest pageRequest);
}
