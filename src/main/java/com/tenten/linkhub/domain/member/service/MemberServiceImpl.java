package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.member.repository.MemberEmailRedisRepository;
import com.tenten.linkhub.domain.member.repository.MemberRepository;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.global.exception.DataDuplicateException;
import com.tenten.linkhub.global.infrastructure.ses.AwsSesService;
import com.tenten.linkhub.global.response.ErrorCode;
import com.tenten.linkhub.global.util.email.EmailDto;
import com.tenten.linkhub.global.util.email.VerificationCodeCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final AwsSesService emailService;
    private final VerificationCodeCreator verificationCodeCreator;
    private final MemberEmailRedisRepository memberEmailRedisRepository;
    private final MemberRepository memberRepository;

    public MemberServiceImpl(AwsSesService emailService,
                             VerificationCodeCreator verificationCodeCreator,
                             MemberEmailRedisRepository memberEmailRedisRepository, MemberRepository memberRepository) {
        this.emailService = emailService;
        this.verificationCodeCreator = verificationCodeCreator;
        this.memberEmailRedisRepository = memberEmailRedisRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public void sendVerificationEmail(EmailDto emailDto) {
        if (memberRepository.existsMemberByNewsEmail(emailDto.getTo())) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_NEWS_EMAIL);
        }

        final String authKey = verificationCodeCreator.createVerificationCode();
        emailService.sendVerificationCodeEmail(emailDto, authKey);
        memberEmailRedisRepository.saveExpire(authKey, emailDto.getTo(), 60 * 3L);
    }

    @Override
    public MailVerificationResponse verificateEmail(MailVerificationRequest request) {
        String emailFoundByCode = memberEmailRedisRepository.getEmail(request.code());
        if (emailFoundByCode == null || !emailFoundByCode.equals(request.email())) {
            return new MailVerificationResponse(false);
        }

        return new MailVerificationResponse(true);
    }

}
