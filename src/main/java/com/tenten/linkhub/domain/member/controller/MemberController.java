package com.tenten.linkhub.domain.member.controller;

import com.tenten.linkhub.domain.member.controller.dto.MailSendApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberJoinApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberJoinApiResponse;
import com.tenten.linkhub.domain.member.controller.mapper.MemberApiMapper;
import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinResponse;
import com.tenten.linkhub.global.response.ErrorResponse;
import com.tenten.linkhub.global.util.email.EmailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "members", description = "member API Document")
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
    @Operation(
            summary = "이메일 발송 API", description = "이메일 발송 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200"),
            })
    @PostMapping("/emails")
    public ResponseEntity<Void> sendMail(@Valid @RequestBody MailSendApiRequest request) {
        EmailDto emailDto = EmailDto.toVerificationEmailDto(request.email());
        memberService.sendVerificationEmail(emailDto);

        return ResponseEntity.ok().build();
    }

    /**
     * 인증 번호를 입력하여 이메일을 인증하는 API
     */
    @Operation(
            summary = "인증 번호를 입력하여 이메일 인증하는 API",
            description = "메일로 전달받은 코드를 인증하는 API입니다. \n인증번호 발송 후 5분이 지났거나 유효하지 않은 인증번호를 발송할 경우 response로 false를 받게 됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200"),
            })
    @PostMapping("/emails/verification")
    public ResponseEntity<MailVerificationApiResponse> verificateMail(@Valid @RequestBody MailVerificationApiRequest apiRequest) {
        MailVerificationRequest request = mapper.toMailVerificationRequest(apiRequest);
        MailVerificationResponse response = memberService.verificateEmail(request);
        MailVerificationApiResponse apiResponse = mapper.toMailVerificatonApiResponse(response);

        return ResponseEntity
                .ok()
                .body(apiResponse);
    }

    /**
     * 회원가입 API
     */
    @Operation(
            summary = "회원가입 API", description = "소셜 아이디, 프로바이더, 닉네임, 자기소개, 프로필 사진을 받아 가입합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입이 성공적으로 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "이미 가입한 회원입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            })
    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberJoinApiResponse> join(
            @Parameter(
                    description = "프로필 사진 외의 데이터는 application/json 형식으로 받습니다."
            )
            @RequestPart @Valid MemberJoinApiRequest request,
            @RequestPart(required = false) MultipartFile file
    ) {
        MemberJoinResponse memberJoinResponse = memberService.join(mapper.toMemberJoinRequest(request, file));

        MemberJoinApiResponse memberJoinApiResponse = MemberJoinApiResponse.from(memberJoinResponse);

        return ResponseEntity.ok(memberJoinApiResponse);
    }

}
