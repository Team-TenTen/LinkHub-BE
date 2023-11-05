package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.auth.service.dto.MemberFindOrCreateResponse;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.global.util.email.EmailDto;
import java.util.List;

public interface MemberService {

    void sendVerificationEmail(EmailDto emailDto);

    MailVerificationResponse verificateEmail(MailVerificationRequest request);

    MemberFindOrCreateResponse findOrCreateMember(String socialId, Provider provider);

    MemberInfos findMemberInfosByMemberIds(List<Long> memberIds);
}
