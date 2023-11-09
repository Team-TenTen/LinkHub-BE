package com.tenten.linkhub.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.MemberEmailRedisRepository;
import com.tenten.linkhub.domain.member.repository.MemberJpaRepository;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinResponse;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
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
    private S3Uploader mockS3Uploader;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

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
        //given
        MockMultipartFile requestFile = new MockMultipartFile("테스트 이미지", (byte[]) null);
        ImageInfo imageInfo = ImageInfo.of("https://testimage", requestFile.getName());
        BDDMockito.given(mockS3Uploader.saveImage(any())).willReturn(imageInfo);

        //when
        MemberJoinResponse response = joinWithMemberRequest(requestFile);

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
        MockMultipartFile requestFile = new MockMultipartFile("테스트 이미지", (byte[]) null);
        ImageInfo imageInfo = ImageInfo.of("https://testimage", requestFile.getName());
        BDDMockito.given(mockS3Uploader.saveImage(any())).willReturn(imageInfo);

        joinWithMemberRequest(requestFile);

        //when && then
        assertThatThrownBy(() -> joinWithMemberRequest(requestFile))
                .isInstanceOf(UnauthorizedAccessException.class)
                .hasMessageContaining("이미 가입한 회원입니다.");
    }

    private MemberJoinResponse joinWithMemberRequest(MockMultipartFile requestFile) {
        return memberService.join(
                new MemberJoinRequest(
                        "32342341232",
                        Provider.kakao,
                        "백둥이",
                        "만나서 반갑습니다.",
                        "baekdoong@gmail.com",
                        Category.KNOWLEDGE_ISSUE_CAREER,
                        true,
                        requestFile
                )
        );
    }
}
