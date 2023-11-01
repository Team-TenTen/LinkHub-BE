package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.global.util.email.EmailDto;

public interface MemberService {

    void sendVerificationEmail(EmailDto emailDto);

    MailVerificationResponse verificateEmail(MailVerificationRequest request);

    Long findOrCreateUser(String socialId, String provider);
}
