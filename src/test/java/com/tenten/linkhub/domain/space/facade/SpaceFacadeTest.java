package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.repository.MemberJpaRepository;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.facade.dto.SpaceMemberDetailInfo;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceCreateRequest;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


@Transactional
@TestPropertySource(locations = "classpath:/application-test.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SpaceFacadeTest {

    @Autowired
    private SpaceFacade spaceFacade;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @MockBean
    private S3Uploader mockS3Uploader;

    private Long setUpSpaceId;
    private Long setUpMemberId;

    @BeforeEach
    void setUp(){
        setUpData();
    }

    @Test
    @DisplayName("유저는 스페이스를 생성할 수 있다.")
    void createSpace(){
        //given
        MockMultipartFile requestFile = new MockMultipartFile("테스트 이미지3", (byte[]) null);
        ImageInfo imageInfo = ImageInfo.of("https://testimage3", requestFile.getName());
        BDDMockito.given(mockS3Uploader.saveImage(any())).willReturn(imageInfo);

        SpaceCreateFacadeRequest spaceCreateFacadeRequest = new SpaceCreateFacadeRequest(
                "테스트용 스페이스 이름",
                "테스트용 스페이스 소개글",
                Category.ENTER_ART,
                true,
                true,
                true,
                true,
                3L,
                requestFile
        );

        //when
        Long savedSpaceId = spaceFacade.createSpace(spaceCreateFacadeRequest);

        //then
        Space savedSpace = spaceJpaRepository.findById(savedSpaceId).get();
        SpaceImage spaceImage = savedSpace.getSpaceImages().get(0);

        assertThat(savedSpace.getSpaceName()).isEqualTo("테스트용 스페이스 이름");
        assertThat(savedSpace.getDescription()).isEqualTo("테스트용 스페이스 소개글");
        assertThat(savedSpace.getCategory()).isEqualTo(Category.ENTER_ART);
        assertThat(spaceImage.getPath()).isEqualTo("https://testimage3");
        assertThat(spaceImage.getName()).isEqualTo("테스트 이미지3");
    }

    @Test
    @DisplayName("유저는 spaceId를 통해 스페이스의 상세 정보를 조회할 수 있다.")
    void getSpaceDetailById() {
        //when
        SpaceDetailGetByIdFacadeResponse response = spaceFacade.getSpaceDetailById(setUpSpaceId, null);

        //then
        List<SpaceMemberDetailInfo> spaceMemberDetailInfos = response.memberDetailInfos();

        assertThat(response.spaceId()).isEqualTo(setUpSpaceId);
        assertThat(response.spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(response.description()).isEqualTo("첫번째 스페이스 소개글");
        assertThat(response.spaceImagePath()).isEqualTo("https://testimage1");
        assertThat(response.viewCount()).isEqualTo(0L);
        assertThat(spaceMemberDetailInfos.get(0).memberId()).isEqualTo(setUpMemberId);
        assertThat(spaceMemberDetailInfos.get(0).nickname()).isEqualTo("잠자는 사자의 콧털");
        assertThat(spaceMemberDetailInfos.get(0).profilePath()).isEqualTo("https://testprofileimage");
    }

    private void setUpData() {
        Member member = new Member(
                "testOAuthEmail",
                com.tenten.linkhub.domain.member.model.Role.USER,
                "잠자는 사자의 콧털",
                "테스트용 소개글",
                "abc@gmail.com",
                true
        );

        member.addProfileImage(
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지")
        );

        setUpMemberId = memberJpaRepository.save(member).getId();

        Space space = new Space(
                setUpMemberId,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                true,
                true,
                true
        );

        space.addSpaceMember(
                new SpaceMember(setUpMemberId, Role.OWNER)
        );

        space.addSpaceImage(
                new SpaceImage("https://testimage1", "테스트 이미지1")
        );

        setUpSpaceId = spaceJpaRepository.save(space).getId();
    }

}