package com.tenten.linkhub.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.tenten.linkhub.domain.auth.JwtProvider;
import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.MemberEmailRedisRepository;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowCreateResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowersFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowingsFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberMyProfileResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberProfileResponse;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.s3.ImageFileUploader;
import com.tenten.linkhub.global.exception.DataDuplicateException;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.yml")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberEmailRedisRepository redisRepository;

    @MockBean
    private ImageFileUploader mockImageFileUploader;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private MockMultipartFile requestFile;

    Long myMemberId;
    Long targetMemberId;
    Long memberIdFollowedByTargetMemberButNotByMyMemberId;
    Long memberIdFollowedByTargetMemberAndMyMemberId;
    Long memberIdFollowingTargetMemberAndFollowedByMyMemberId;
    Long memberIdFollowingTargetMemberButNotFollowedByMyMemberId;
    MemberJoinRequest myMemberRequest;
    MemberJoinRequest targetMemberRequest;

    @BeforeEach
    void setUp() {
        setUpData();
    }

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

    @Test
    @DisplayName("사용자는 최초로 회원가입을 성공한다.")
    void join_MemberJoinRequest_Success() {
        //given && when
        MemberJoinResponse response = memberService.join(createMemberJoinRequest(requestFile));

        //then
        assertThat(response.jwt())
                .isNotNull()
                .contains(".")
                .doesNotContain(" ");
    }

    @Test
    @DisplayName("이미 등록된 사용자는 회원 가입에 실패한다.")
    void join_MemberJoinRequest_Fail() {
        //given
        MemberJoinResponse response = memberService.join(createMemberJoinRequest(requestFile));

        //when && then
        assertThatThrownBy(() -> memberService.join(createMemberJoinRequest(requestFile)))
                .isInstanceOf(UnauthorizedAccessException.class)
                .hasMessageContaining("이미 가입한 회원입니다.");
    }

    @Test
    @DisplayName("사용자는 타인의 프로필 조회 및 팔로잉 유무를 전달 받는다.")
    void getProfile_MemberIdAndMyMemberId_SuccessWithFollowingStatus() {
        //given
        memberService.createFollow(targetMemberId, myMemberId);

        // when
        MemberProfileResponse response = memberService.getProfile(targetMemberId, myMemberId);

        //then
        assertThat(response.memberId()).isEqualTo(targetMemberId);
        assertThat(response.aboutMe()).isEqualTo(targetMemberRequest.aboutMe());
        assertThat(response.nickname()).isEqualTo(targetMemberRequest.nickname());
        assertThat(response.newsEmail()).isEqualTo(targetMemberRequest.newsEmail());
        assertThat(response.followerCount()).isNotNegative();
        assertThat(response.followingCount()).isNotNegative();
        assertThat(response.favoriteCategory()).isEqualTo(targetMemberRequest.favoriteCategory());
        assertThat(response.isFollowing()).isTrue();
        assertThat(response.isModifiable()).isFalse();
    }

    @Test
    @DisplayName("사용자는 자신의 프로필 조회 및 수정 권한을 전달 받는다.")
    void getProfile_MemberIdAndMyMemberId_SuccessWithModificationStatus() {
        //given //when
        MemberProfileResponse response = memberService.getProfile(myMemberId, myMemberId);

        //then
        assertThat(response.memberId()).isEqualTo(myMemberId);
        assertThat(response.aboutMe()).isEqualTo(myMemberRequest.aboutMe());
        assertThat(response.nickname()).isEqualTo(myMemberRequest.nickname());
        assertThat(response.newsEmail()).isEqualTo(myMemberRequest.newsEmail());
        assertThat(response.followerCount()).isNotNegative();
        assertThat(response.followingCount()).isNotNegative();
        assertThat(response.favoriteCategory()).isEqualTo(myMemberRequest.favoriteCategory());
        assertThat(response.isFollowing()).isFalse();
        assertThat(response.isModifiable()).isTrue();
    }

    @Test
    @DisplayName("사용자는 멤버가 존재하지 않아 프로필 조회에 실패한다.")
    void getProfile_MemberProfileRequest_Fail() {
        //given
        Long notExistingMemberId = 999L;

        // when //then
        assertThatThrownBy(() -> memberService.getProfile(notExistingMemberId, myMemberId))
                .isInstanceOf(UnauthorizedAccessException.class)
                .hasMessageContaining("존재하지 않는 회원입니다.");
    }

    @Test
    @DisplayName("사용자는 마이페이지 조회를 성공한다.")
    void getMyProfile_MyMemberId_Success() {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest(requestFile);
        MemberJoinResponse memberJoinResponse = memberService.join(memberJoinRequest);

        Authentication authentication = jwtProvider.getAuthentication(memberJoinResponse.jwt());
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        //when
        MemberMyProfileResponse response = memberService.getMyProfile(memberDetails.memberId());

        //then
        assertThat(response.memberId()).isEqualTo(memberDetails.memberId());
        assertThat(response.aboutMe()).isEqualTo(memberJoinRequest.aboutMe());
        assertThat(response.nickname()).isEqualTo(memberJoinRequest.nickname());
        assertThat(response.newsEmail()).isEqualTo(memberJoinRequest.newsEmail());
        assertThat(response.followerCount()).isNotNegative();
        assertThat(response.followingCount()).isNotNegative();
        assertThat(response.favoriteCategory()).isEqualTo(memberJoinRequest.favoriteCategory());
    }

    @Test
    @DisplayName("사용자는 팔로우를 할 수 있다.")
    void createFollow_TargetMemberIdAndMyMemberId_Success() {
        //given //when
        MemberFollowCreateResponse response = memberService.createFollow(targetMemberId, myMemberId);

        //then
        assertThat(response.followedId()).isEqualTo(targetMemberId);
    }

    @Test
    @DisplayName("사용자는 존재하지 않은 유저의 팔로우를 실패한다.")
    void createFollow_TargetMemberIdAndMyMemberId_DataNotFoundException() {
        //given
        Long notExistingMemberId = 999L;

        // when //then
        assertThatThrownBy(() -> memberService.createFollow(notExistingMemberId, myMemberId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessageContaining("존재하지 않는 유저입니다.");
    }

    @Test
    @DisplayName("사용자는 이미 팔로우한 유저의 팔로우를 실패한다.")
    void createFollow_TargetMemberIdAndMyMemberId_UnauthorizedException() {
        //given
        memberService.createFollow(targetMemberId, myMemberId);

        //when //then
        assertThatThrownBy(() -> memberService.createFollow(targetMemberId, myMemberId))
                .isInstanceOf(DataDuplicateException.class);
    }

    @Test
    @DisplayName("사용자는 언팔로우를 할 수 있다.")
    void deleteFollow_TargetMemberIdAndMyMemberId_Success() {
        //given
        memberService.createFollow(targetMemberId, myMemberId);

        //when
        Long deletedTargetId = memberService.deleteFollow(targetMemberId, myMemberId);

        //then
        assertThat(deletedTargetId).isEqualTo(targetMemberId);
    }

    @Test
    @DisplayName("사용자는 존재하지 않는 유저의 언팔로우를 실패한다.")
    void deleteFollowForNotExistingUser_TargetMemberIdAndMyMemberId_UnauthorizedException() {
        //given
        Long notExistingMemberId = 999L;

        // when //then
        assertThatThrownBy(() -> memberService.deleteFollow(notExistingMemberId, myMemberId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessageContaining("존재하지 않는 팔로우 또는 유저입니다.");
    }

    @Test
    @DisplayName("사용자는 존재하지 않는 팔로우의 언팔로우를 실패한다.")
    void deleteFollowForNotExistingFollow_TargetMemberIdAndMyMemberId_UnauthorizedException() {
        //given //when //then
        assertThatThrownBy(() -> memberService.deleteFollow(targetMemberId, myMemberId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessageContaining("존재하지 않는 팔로우 또는 유저입니다.");
    }

    @Test
    @DisplayName("사용자는 특정 유저가 팔로잉한 유저를 페이징 조회한다.")
    void getFollowings_TargetMemberIdAndMyMemberId_Success() {
        //given
        memberService.createFollow(memberIdFollowedByTargetMemberAndMyMemberId, targetMemberId);
        memberService.createFollow(memberIdFollowedByTargetMemberAndMyMemberId, myMemberId);
        memberService.createFollow(memberIdFollowedByTargetMemberButNotByMyMemberId, targetMemberId);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        MemberFollowingsFindResponses responses = memberService.getFollowings(targetMemberId, myMemberId, pageRequest);

        //then
        assertThat(responses.responses().getContent().get(0).memberId()).isEqualTo(
                memberIdFollowedByTargetMemberAndMyMemberId);
        assertThat(responses.responses().getContent().get(0).aboutMe()).isEqualTo(
                "타겟 유저가 팔로우하고 나도 팔로우한 유저");
        assertThat(responses.responses().getContent().get(0).isFollowing()).isTrue();

        assertThat(responses.responses().getContent().get(1).memberId()).isEqualTo(
                memberIdFollowedByTargetMemberButNotByMyMemberId);
        assertThat(responses.responses().getContent().get(1).aboutMe()).isEqualTo(
                "타겟 유저가 팔로우했지만 나는 안한 유저");
        assertThat(responses.responses().getContent().get(1).isFollowing()).isFalse();
    }

    @Test
    @DisplayName("사용자는 특정 유저의 팔로워들을 페이징 조회한다.")
    void getFollowers_TargetMemberIdAndMyMemberId_Success() {
        //given
        memberService.createFollow(targetMemberId, memberIdFollowingTargetMemberButNotFollowedByMyMemberId);
        memberService.createFollow(targetMemberId, memberIdFollowingTargetMemberAndFollowedByMyMemberId);
        memberService.createFollow(memberIdFollowingTargetMemberAndFollowedByMyMemberId, myMemberId);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        MemberFollowersFindResponses responses = memberService.getFollowers(targetMemberId, myMemberId, pageRequest);

        //then
        assertThat(responses.responses().getContent().get(0).memberId()).isEqualTo(
                memberIdFollowingTargetMemberButNotFollowedByMyMemberId);
        assertThat(responses.responses().getContent().get(0).aboutMe()).isEqualTo(
                "내가 팔로우하지 않는 타겟 유저를 팔로우한 유저");
        assertThat(responses.responses().getContent().get(0).isFollowing()).isFalse();

        assertThat(responses.responses().getContent().get(1).memberId()).isEqualTo(
                memberIdFollowingTargetMemberAndFollowedByMyMemberId);
        assertThat(responses.responses().getContent().get(1).aboutMe()).isEqualTo(
                "내가 팔로우하는 타겟 유저를 팔로우한 유저");
        assertThat(responses.responses().getContent().get(1).isFollowing()).isTrue();
    }

    @Test
    @DisplayName("사용자가 특정 유저의 팔로잉 목록이 전부 삭제된 유저인 경우 빈 리스트를 돌려 받는다.")
    void getFollowingsOfDeletedUsers_TargetMemberIdAndMyMemberId_SuccessWithEmptyList() {
        //given
        memberService.createFollow(memberIdFollowedByTargetMemberAndMyMemberId, targetMemberId);
        memberService.createFollow(memberIdFollowedByTargetMemberAndMyMemberId, myMemberId);
        memberService.createFollow(memberIdFollowedByTargetMemberButNotByMyMemberId, targetMemberId);

        Optional<Member> memberFollowedByTargetMemberAndMyMemberIdOptional = memberJpaRepository.findById(memberIdFollowedByTargetMemberAndMyMemberId);
        Optional<Member> memberFollowedByTargetMemberButNotByMyMemberIdOptional = memberJpaRepository.findById(memberIdFollowedByTargetMemberButNotByMyMemberId);

        Member memberFollowedByTargetMemberAndMyMemberId = memberFollowedByTargetMemberAndMyMemberIdOptional.get();
        Member memberFollowedByTargetMemberButNotByMyMemberId = memberFollowedByTargetMemberButNotByMyMemberIdOptional.get();

        memberFollowedByTargetMemberAndMyMemberId.deleteMember();
        memberFollowedByTargetMemberButNotByMyMemberId.deleteMember();

        memberJpaRepository.save(memberFollowedByTargetMemberAndMyMemberId);
        memberJpaRepository.save(memberFollowedByTargetMemberButNotByMyMemberId);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        MemberFollowingsFindResponses responses = memberService.getFollowings(targetMemberId, myMemberId, pageRequest);

        //then
        assertThat(responses.responses().getContent()).isEmpty();
    }

    @Test
    @DisplayName("사용자가 특정 유저의 팔로워 목록이 전부 삭제된 유저인 경우 빈 리스트를 돌려 받는다.")
    void getFollowersOfDeletedUsers_TargetMemberIdAndMyMemberId_SuccessWithEmptyList() {
        //given
        memberService.createFollow(targetMemberId, memberIdFollowingTargetMemberButNotFollowedByMyMemberId);
        memberService.createFollow(targetMemberId, memberIdFollowingTargetMemberAndFollowedByMyMemberId);
        memberService.createFollow(memberIdFollowingTargetMemberAndFollowedByMyMemberId, myMemberId);

        Optional<Member> memberFollowingTargetMemberButNotFollowedByMyMemberIdOptional = memberJpaRepository.findById(memberIdFollowingTargetMemberButNotFollowedByMyMemberId);
        Optional<Member> memberFollowingTargetMemberAndFollowedByMyMemberIdOptional = memberJpaRepository.findById(memberIdFollowingTargetMemberAndFollowedByMyMemberId);

        Member memberFollowingTargetMemberButNotFollowedByMyMemberId = memberFollowingTargetMemberButNotFollowedByMyMemberIdOptional.get();
        Member memberFollowingTargetMemberAndFollowedByMyMemberId = memberFollowingTargetMemberAndFollowedByMyMemberIdOptional.get();

        memberFollowingTargetMemberButNotFollowedByMyMemberId.deleteMember();
        memberFollowingTargetMemberAndFollowedByMyMemberId.deleteMember();

        memberJpaRepository.save(memberFollowingTargetMemberButNotFollowedByMyMemberId);
        memberJpaRepository.save(memberFollowingTargetMemberAndFollowedByMyMemberId);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        MemberFollowersFindResponses responses = memberService.getFollowers(targetMemberId, myMemberId, pageRequest);

        //then
        assertThat(responses.responses().getContent()).isEmpty();
    }

    @Test
    @DisplayName("요청 유저 Id가 null 인 경우 특정 유저의 팔로잉 목록 조회 시 팔로잉 유무에 전부 false를 내려준다.")
    void getFollowings_TargetMemberIdAndNullMemberId_SuccessWithFollowingStatusAllFalse() {
        //given
        memberService.createFollow(memberIdFollowedByTargetMemberAndMyMemberId, targetMemberId);
        memberService.createFollow(memberIdFollowedByTargetMemberAndMyMemberId, myMemberId);
        memberService.createFollow(memberIdFollowedByTargetMemberButNotByMyMemberId, targetMemberId);

        PageRequest pageRequest = PageRequest.of(0, 10);

        Long myMemberId = null;

        //when
        MemberFollowingsFindResponses responses = memberService.getFollowings(targetMemberId, myMemberId, pageRequest);

        //then
        assertThat(responses.responses().getContent().get(0).memberId()).isEqualTo(
                memberIdFollowedByTargetMemberAndMyMemberId);
        assertThat(responses.responses().getContent().get(0).isFollowing()).isFalse();

        assertThat(responses.responses().getContent().get(1).memberId()).isEqualTo(
                memberIdFollowedByTargetMemberButNotByMyMemberId);
        assertThat(responses.responses().getContent().get(1).isFollowing()).isFalse();
    }

    @Test
    @DisplayName("요청 유저 Id가 null 인 경우 특정 유저의 팔로워 목록 조회 시 팔로잉 유무에 전부 false를 내려준다.")
    void getFollowers_TargetMemberIdAndNullMemberId_SuccessWithFollowingStatusAllFalse() {
        //given
        memberService.createFollow(targetMemberId, memberIdFollowingTargetMemberButNotFollowedByMyMemberId);
        memberService.createFollow(targetMemberId, memberIdFollowingTargetMemberAndFollowedByMyMemberId);
        memberService.createFollow(memberIdFollowingTargetMemberAndFollowedByMyMemberId, myMemberId);

        PageRequest pageRequest = PageRequest.of(0, 10);

        Long myMemberId = null;

        //when
        MemberFollowersFindResponses responses = memberService.getFollowers(targetMemberId, myMemberId, pageRequest);

        //then
        assertThat(responses.responses().getContent().get(0).memberId()).isEqualTo(
                memberIdFollowingTargetMemberButNotFollowedByMyMemberId);
        assertThat(responses.responses().getContent().get(0).isFollowing()).isFalse();

        assertThat(responses.responses().getContent().get(1).memberId()).isEqualTo(
                memberIdFollowingTargetMemberAndFollowedByMyMemberId);
        assertThat(responses.responses().getContent().get(1).isFollowing()).isFalse();
    }

    private MemberJoinRequest createMemberJoinRequest(MockMultipartFile requestFile) {
        return new MemberJoinRequest(
                "32342341912",
                Provider.kakao,
                "백둥이",
                "만나서 반갑습니다.",
                "linkhub@link-hub.site",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                requestFile
        );
    }

    private void setUpData() {
        MockMultipartFile requestFile = new MockMultipartFile("테스트 이미지", (byte[]) null);
        ImageInfo imageInfo = ImageInfo.of("https://testimage", requestFile.getName());
        BDDMockito.given(mockImageFileUploader.saveImage(any())).willReturn(imageInfo);

        myMemberRequest = new MemberJoinRequest(
                "32342341231",
                Provider.kakao,
                "멤버1",
                "내 유저",
                "linkhub@link-hub.site",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                requestFile
        );

        targetMemberRequest = new MemberJoinRequest(
                "32342341232",
                Provider.kakao,
                "멤버2",
                "타겟 유저",
                "linkhub@link-hub.site",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                requestFile
        );

        MemberJoinRequest memberFollowedByTargetMemberButNotByMyMemberRequest = new MemberJoinRequest(
                "32342341233",
                Provider.kakao,
                "멤버3",
                "타겟 유저가 팔로우했지만 나는 안한 유저",
                "linkhub@link-hub.site",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                requestFile
        );

        MemberJoinRequest memberFollowedByTargetMemberAndMyMemberRequest = new MemberJoinRequest(
                "32342341234",
                Provider.kakao,
                "멤버4",
                "타겟 유저가 팔로우하고 나도 팔로우한 유저",
                "linkhub@link-hub.site",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                requestFile
        );

        MemberJoinRequest memberFollowingTargetMemberAndFollowedByMyMemberRequest = new MemberJoinRequest(
                "32342341235",
                Provider.kakao,
                "멤버5",
                "내가 팔로우하는 타겟 유저를 팔로우한 유저",
                "linkhub@link-hub.site",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                requestFile
        );

        MemberJoinRequest memberFollowingTargetMemberButNotFollowedByMyMemberRequest = new MemberJoinRequest(
                "32342341236",
                Provider.kakao,
                "멤버6",
                "내가 팔로우하지 않는 타겟 유저를 팔로우한 유저",
                "linkhub@link-hub.site",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                requestFile
        );

        MemberJoinResponse myMemberResponse = memberService.join(myMemberRequest);
        MemberJoinResponse targetMemberResponse = memberService.join(targetMemberRequest);
        MemberJoinResponse memberFollowedByTargetMemberButNotByMyMemberResponse = memberService.join(
                memberFollowedByTargetMemberButNotByMyMemberRequest);
        MemberJoinResponse memberFollowedByTargetMemberAndMyMemberResponse = memberService.join(
                memberFollowedByTargetMemberAndMyMemberRequest);
        MemberJoinResponse memberFollowingTargetMemberAndFollowedByMyMemberResponse = memberService.join(
                memberFollowingTargetMemberAndFollowedByMyMemberRequest);
        MemberJoinResponse memberFollowingTargetMemberButNotFollowedByMyMemberResponse = memberService.join(
                memberFollowingTargetMemberButNotFollowedByMyMemberRequest);

        Authentication myMemberAuthentication = jwtProvider.getAuthentication(myMemberResponse.jwt());
        Authentication targetMemberAuthentication = jwtProvider.getAuthentication(targetMemberResponse.jwt());
        Authentication memberFollowedByTargetMemberButNotByMyMemberAuthentication = jwtProvider.getAuthentication(
                memberFollowedByTargetMemberButNotByMyMemberResponse.jwt());
        Authentication memberFollowedByTargetMemberAndMyMemberAuthentication = jwtProvider.getAuthentication(
                memberFollowedByTargetMemberAndMyMemberResponse.jwt());
        Authentication memberFollowingTargetMemberAndFollowedByMyMemberAuthentication = jwtProvider.getAuthentication(
                memberFollowingTargetMemberAndFollowedByMyMemberResponse.jwt());
        Authentication memberFollowingTargetMemberButNotFollowedByMyMemberAuthentication = jwtProvider.getAuthentication(
                memberFollowingTargetMemberButNotFollowedByMyMemberResponse.jwt());

        MemberDetails myMemberDetails = (MemberDetails) myMemberAuthentication.getPrincipal();
        MemberDetails targetMemberDetails = (MemberDetails) targetMemberAuthentication.getPrincipal();
        MemberDetails memberFollowedByTargetMemberButNotByMyMemberDetails = (MemberDetails) memberFollowedByTargetMemberButNotByMyMemberAuthentication.getPrincipal();
        MemberDetails memberFollowedByTargetMemberAndMyMemberDetails = (MemberDetails) memberFollowedByTargetMemberAndMyMemberAuthentication.getPrincipal();
        MemberDetails memberFollowingTargetMemberAndFollowedByMyMemberDetails = (MemberDetails) memberFollowingTargetMemberAndFollowedByMyMemberAuthentication.getPrincipal();
        MemberDetails memberFollowingTargetMemberButNotFollowedByMyMemberDetails = (MemberDetails) memberFollowingTargetMemberButNotFollowedByMyMemberAuthentication.getPrincipal();

        myMemberId = myMemberDetails.memberId();
        targetMemberId = targetMemberDetails.memberId();
        memberIdFollowedByTargetMemberButNotByMyMemberId = memberFollowedByTargetMemberButNotByMyMemberDetails.memberId();
        memberIdFollowedByTargetMemberAndMyMemberId = memberFollowedByTargetMemberAndMyMemberDetails.memberId();
        memberIdFollowingTargetMemberAndFollowedByMyMemberId = memberFollowingTargetMemberAndFollowedByMyMemberDetails.memberId();
        memberIdFollowingTargetMemberButNotFollowedByMyMemberId = memberFollowingTargetMemberButNotFollowedByMyMemberDetails.memberId();
    }

}
