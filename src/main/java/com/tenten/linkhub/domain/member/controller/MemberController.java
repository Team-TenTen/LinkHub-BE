package com.tenten.linkhub.domain.member.controller;

import com.tenten.linkhub.domain.member.controller.dto.MailSendApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiResponse;
import com.tenten.linkhub.domain.member.controller.mapper.MemberApiMapper;
import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.global.util.email.EmailDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberApiMapper mapper;

    public MemberController(MemberService memberService, MemberApiMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    /**
     * 회원가입 또는 이메일 변경 시 메일로 인증번호 발송하는 API
     */
    @PostMapping("/emails")
    public ResponseEntity<Void> sendMail(@Valid @RequestBody MailSendApiRequest request) {
        EmailDto emailDto = EmailDto.toVerificationEmailDto(request.email());
        memberService.sendVerificationEmail(emailDto);

        return ResponseEntity.ok().build();
    }

    /**
     * 인증 번호를 입력하여 이메일을 인증하는 API
     */
    @PostMapping("/emails/verification")
    public ResponseEntity<MailVerificationApiResponse> verificateMail(@Valid @RequestBody MailVerificationApiRequest apiRequest) {
        MailVerificationRequest request = mapper.toMailVerificationRequest(apiRequest);
        MailVerificationResponse response = memberService.verificateEmail(request);
        MailVerificationApiResponse apiResponse = mapper.toMailVerificatonApiResponse(response);

        return ResponseEntity
                .ok()
                .body(apiResponse);
    }

}
