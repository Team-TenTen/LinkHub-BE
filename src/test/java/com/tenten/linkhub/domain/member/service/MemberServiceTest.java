package com.tenten.linkhub.domain.member.service;

import com.tenten.linkhub.domain.member.repository.MemberEmailRedisRepository;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberEmailRedisRepository redisRepository;

    @AfterEach
    void tearDown() {
        redisRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자는 5분 이내에 올바른 인증번호를 입력할 경우 이메일 인증에 성공한다.")
    void verificateEmail_EmailAndCode_Success() {
        //given
        String code = "123456";
        String email = "linkhub@link-hub.site";
        setUpEmailAndCodeInRedis(code, email);

        //when
        MailVerificationResponse response = memberService.verificateEmail(new MailVerificationRequest(email, code));

        //then
        Assertions.assertThat(response.isVerificate()).isTrue();
    }

    @Test
    @DisplayName("사용자는 올바르지 않은 인증번호를 입력할 경우 이메일 인증에 실패한다.")
    void verificateEmail_EmailAndCode_Fail() {
        //given
        String code = "123456";
        String strangeCode = "000000";
        String email = "linkhub@link-hub.site";
        setUpEmailAndCodeInRedis(code, email);

        //when
        MailVerificationResponse response = memberService.verificateEmail(new MailVerificationRequest(email, strangeCode));

        //then
        Assertions.assertThat(response.isVerificate()).isFalse();
    }

    private void setUpEmailAndCodeInRedis(String code, String email) {
        redisRepository.save(code, email);
    }

}
