package com.tenten.linkhub.domain.member.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.member.controller.dto.MailSendApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailSendApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberFollowCreateApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberFollowFindApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberFollowersFindApiResponses;
import com.tenten.linkhub.domain.member.controller.dto.MemberFollowingsFindApiResponses;
import com.tenten.linkhub.domain.member.controller.dto.MemberJoinApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberJoinApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberMyProfileApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberProfileApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberSearchApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberSearchApiResponses;
import com.tenten.linkhub.domain.member.controller.dto.MemberSpacesFindApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberSpacesFindApiResponses;
import com.tenten.linkhub.domain.member.controller.dto.MemberUpdateApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberUpdateApiResponse;
import com.tenten.linkhub.domain.member.controller.mapper.MemberApiMapper;
import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MailSendResponse;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowCreateResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowersFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowingsFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberMyProfileResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberProfileResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberSearchResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberUpdateResponse;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.global.response.ErrorResponse;
import com.tenten.linkhub.global.util.email.EmailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Objects;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final SpaceService spaceService;
    private final MemberApiMapper mapper;

    public MemberController(MemberService memberService, SpaceService spaceService, MemberApiMapper mapper) {
        this.memberService = memberService;
        this.spaceService = spaceService;
        this.mapper = mapper;
    }

    /**
     * 회원가입 또는 이메일 변경 시 메일로 인증번호 발송하는 API
     */
    @Operation(
            summary = "이메일 발송 API", description = "이메일 발송 API 입니다. 발송한 이메일 내의 인증코드는 5분간 유효하며 성공적으로 뱔송될 경우 받는 응답값(emailCodeDuration(단위: 초))은 남은 인증 시간을 의미합니다.",
            responses = {
                    @ApiResponse(responseCode = "200"),
            })
    @PostMapping("/emails")
    public ResponseEntity<MailSendApiResponse> sendMail(@Valid @RequestBody MailSendApiRequest request) {
        EmailDto emailDto = EmailDto.toVerificationEmailDto(request.email());

        MailSendResponse response = memberService.sendVerificationEmail(emailDto);
        MailSendApiResponse apiResponse = mapper.toMailSendApiResponse(response);

        return ResponseEntity
                .ok()
                .body(apiResponse);
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

    /**
     * 사용자 프로필 조회 API
     */
    @Operation(
            summary = "사용자 프로필 조회 API", description = "멤버 아이디를 받아 프로필을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 조회를 완료하였습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 회원입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            })
    @GetMapping(value = "/{memberId}/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberProfileApiResponse> getProfile(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long memberId
    ) {
        Long myMemberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();

        MemberProfileResponse memberProfileResponse = memberService.getProfile(memberId, myMemberId);

        MemberProfileApiResponse memberProfileApiResponse = mapper.toMemberProfileApiResponse(memberProfileResponse);

        return ResponseEntity.ok(memberProfileApiResponse);
    }

    /**
     * 내 프로필 조회 API
     */
    @Operation(
            summary = "내 프로필 조회 API", description = "JWT를 받아 자신의 프로필을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 조회를 완료하였습니다."),
            })
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberMyProfileApiResponse> getMyProfile(
            @AuthenticationPrincipal MemberDetails memberDetails) {
        MemberMyProfileResponse memberMyProfileResponse = memberService.getMyProfile(memberDetails.memberId());

        MemberMyProfileApiResponse memberMyProfileApiResponse = mapper.toMemberMyProfileApiResponse(
                memberMyProfileResponse);

        return ResponseEntity.ok(memberMyProfileApiResponse);
    }

    /**
     * 내 프로필 수정 API
     */
    @Operation(
            summary = "내 프로필 수정 API", description = "멤버ID 및 JWT를 받아 자신의 프로필을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 수정을 완료하였습니다."),
                    @ApiResponse(responseCode = "404", description = "변경 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            })
    @PutMapping(value = "/{memberId}/profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberUpdateApiResponse> updateMember(
            @Parameter(
                    description = "프로필 사진 외의 데이터는 application/json 형식으로 받습니다."
            )
            @PathVariable Long memberId,
            @RequestPart @Valid MemberUpdateApiRequest request,
            @RequestPart(required = false) MultipartFile file,
            @AuthenticationPrincipal MemberDetails memberDetails) {

        MemberUpdateResponse memberUpdateResponse = memberService.updateProfile(
                mapper.toMemberUpdateRequest(request, file, memberId, memberDetails.memberId()));

        MemberUpdateApiResponse memberUpdateApiResponse = mapper.toMemberUpdateApiResponse(memberUpdateResponse);

        return ResponseEntity.ok(memberUpdateApiResponse);
    }

    /**
     * 유저 팔로우 API
     */
    @Operation(
            summary = "유저 팔로우 API", description = "헤더에 JWT 토큰과 Path Variable로 memberId를 받으며, 요청 Body는 없습니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "팔로우가 성공적으로 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다. / 이미 팔로우된 유저입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            })
    @PostMapping(value = "/{memberId}/follow", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberFollowCreateApiResponse> createFollow(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long memberId
    ) {
        MemberFollowCreateResponse memberFollowCreateResponse = memberService.createFollow(memberId,
                memberDetails.memberId());

        MemberFollowCreateApiResponse memberFollowCreateApiResponse = mapper.toMemberFollowCreateApiResponse(
                memberFollowCreateResponse);

        return ResponseEntity.ok(memberFollowCreateApiResponse);
    }

    /**
     * 유저 언팔로우 API
     */
    @Operation(
            summary = "유저 언팔로우 API", description = "헤더에 JWT 토큰과 Path Variable로 memberId를 받으며, 요청 Body는 없습니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "언팔로우가 성공적으로 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 팔로우 또는 유저입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{memberId}/follow")
    public ResponseEntity<Void> deleteFollow(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long memberId
    ) {
        memberService.deleteFollow(memberId, memberDetails.memberId());

        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * 팔로잉 목록 페이징 조회 API
     */
    @Operation(
            summary = "팔로잉 목록 페이징 조회 API", description = "헤더에 JWT를 옵션으로 받고 Path Variable로 memberId를 받으며, 요청 Body는 없습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로잉 조회를 완료하였습니다.")
            })
    @GetMapping(value = "/{memberId}/following", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberFollowingsFindApiResponses> getFollowings(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long memberId,
            @ModelAttribute MemberFollowFindApiRequest request

    ) {
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize());

        Long myMemberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();

        MemberFollowingsFindResponses memberFollowingsFindResponses = memberService.getFollowings(
                memberId,
                myMemberId,
                pageRequest
        );

        return ResponseEntity.ok(MemberFollowingsFindApiResponses.from(memberFollowingsFindResponses));
    }

    /**
     * 팔로워 목록 페이징 조회 API
     */
    @Operation(
            summary = "팔로워 목록 페이징 조회 API", description = "헤더에 JWT를 옵션으로 받고 토큰 Path Variable로 memberId를 받으며, 요청 Body는 없습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로워 조회를 완료하였습니다.")
            })
    @GetMapping(value = "/{memberId}/followers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberFollowersFindApiResponses> getFollowers(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long memberId,
            @ModelAttribute MemberFollowFindApiRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize());

        Long myMemberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();

        MemberFollowersFindResponses memberFollowersFindResponses = memberService.getFollowers(
                memberId,
                myMemberId,
                pageRequest
        );

        return ResponseEntity.ok(MemberFollowersFindApiResponses.from(
                memberFollowersFindResponses));
    }

    /**
     * 특정 멤버 스페이스 검색 API
     */
    @Operation(
            summary = "특정 멤버 스페이스 검색 API", description = "특정 멤버의 스페이스를 keyWord, pageNumber, pageSize, filter를 통해 검색합니다. (keyWord, sort, filter 조건 없이 사용 가능합니다.)\n\n" +
            "!!해당 API는 나의 스페이스 조회와 다른 유저의 스페이스 조회에 사용되며 나의 스페이스인지 여부는 토큰값과 member식별자를 통해 이루어 집니다.!!\n\n" +
            "해당 API는 keyWord, filter 없이도 사용 가능한 페이징 조회입니다.\n\n" +
            "sort: {created_at, updated_at, favorite_count, view_count}\n\n" +
            "filter: {ENTER_ART, LIFE_KNOWHOW_SHOPPING, HOBBY_LEISURE_TRAVEL, KNOWLEDGE_ISSUE_CAREER, ETC}",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색이 성공적으로 완료 되었습니다."),
            })
    @GetMapping(value = "/{memberId}/spaces/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberSpacesFindApiResponses> findMySpaces(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long memberId,
            @ModelAttribute MemberSpacesFindApiRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize());
        Long requestMemberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();

        SpacesFindByQueryResponses responses = spaceService.findMemberSpacesByQuery(
                mapper.toMemberSpacesFindRequest(pageRequest, request, requestMemberId, memberId)
        );

        MemberSpacesFindApiResponses apiResponses = MemberSpacesFindApiResponses.from(responses);
        return ResponseEntity.ok(apiResponses);
    }

    /**
     * 멤버 검색 API
     */
    @Operation(
            summary = "멤버 검색 API", description = "특정 키워드를 기준으로 멤버를 검색합니다. keyWord, pageNumber, pageSize를 통해 검색합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색이 성공적으로 완료 되었습니다."),
            })
    @GetMapping(value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberSearchApiResponses> searchMember(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @ModelAttribute MemberSearchApiRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize());

        Long myMemberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();

        MemberSearchResponses responses = memberService.searchMember(
                mapper.toMemberSearchRequest(request, pageRequest, myMemberId)
        );

        MemberSearchApiResponses apiResponses = MemberSearchApiResponses.from(responses);
        return ResponseEntity.ok(apiResponses);
    }

}
