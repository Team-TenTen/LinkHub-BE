package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFindResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberProfileResponse;
import com.tenten.linkhub.global.util.email.EmailDto;

import java.util.List;

public interface MemberService {

    void sendVerificationEmail(EmailDto emailDto);

    MailVerificationResponse verificateEmail(MailVerificationRequest request);

    MemberFindResponse findMember(String socialId, Provider provider);

    MemberInfos findMemberInfosByMemberIds(List<Long> memberIds);

    MemberJoinResponse join(MemberJoinRequest memberJoinRequest);

    MemberProfileResponse getProfile(Long memberId);
}
