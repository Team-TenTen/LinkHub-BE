package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.facade.dto.SpaceMemberDetailInfo;
import com.tenten.linkhub.domain.space.facade.dto.SpaceUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.handler.SpaceEventHandler;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@Transactional
@TestPropertySource(locations = "classpath:/application-test.yml")
@SpringBootTest
class SpaceFacadeTest {

    @Autowired
    private SpaceFacade spaceFacade;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @MockBean
    private S3Uploader mockS3Uploader;

    @MockBean
    private SpaceEventHandler spaceEventHandler;

    private Long setUpSpaceId;
    private Long setUpMemberId;

    @BeforeEach
    void setUp() {
        setUpData();
    }

    @Test
    @DisplayName("유저는 스페이스를 생성할 수 있다.")
    void createSpace() {
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
        //given
        SpaceDetailGetByIdFacadeRequest request = new SpaceDetailGetByIdFacadeRequest(
                setUpSpaceId,
                setUpMemberId,
                new ArrayList<>());

        //when
        SpaceDetailGetByIdFacadeResponse response = spaceFacade.getSpaceDetailById(request);

        //then
        List<SpaceMemberDetailInfo> spaceMemberDetailInfos = response.memberDetailInfos();

        assertThat(response.spaceId()).isEqualTo(setUpSpaceId);
        assertThat(response.spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(response.description()).isEqualTo("첫번째 스페이스 소개글");
        assertThat(response.spaceImagePath()).isEqualTo("https://testimage1");
        assertThat(response.viewCount()).isEqualTo(0L);
        assertThat(response.isOwner()).isEqualTo(true);
        assertThat(spaceMemberDetailInfos.get(0).memberId()).isEqualTo(setUpMemberId);
        assertThat(spaceMemberDetailInfos.get(0).nickname()).isEqualTo("잠자는 사자의 콧털");
        assertThat(spaceMemberDetailInfos.get(0).profilePath()).isEqualTo("https://testprofileimage");
    }

    @Test
    @DisplayName("프라이빗 스페이스의 상세 조회 시 권한이 없는 유저는 UnauthorizedAccessException가 발생한다.")
    void getSpaceDetailById_UnauthorizedAccessException() {
        //given
        SpaceDetailGetByIdFacadeRequest request = new SpaceDetailGetByIdFacadeRequest(
                setUpSpaceId,
                setUpMemberId + 100,
                new ArrayList<>());

        //when//then
        assertThatThrownBy(() -> spaceFacade.getSpaceDetailById(request))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("유저는 스페이스의 정보들을 변경할 수 있다.")
    void updateSpace() {
        //given
        ImageInfo imageInfo = ImageInfo.of("https://testimage3", "테스트 이미지 파일 이름");
        BDDMockito.given(mockS3Uploader.saveImage(any())).willReturn(imageInfo);

        SpaceUpdateFacadeRequest request = new SpaceUpdateFacadeRequest(
                setUpSpaceId,
                "업데이트 스페이스 네임",
                "업데이트 스페이스 소개글",
                Category.HOBBY_LEISURE_TRAVEL,
                false,
                false,
                false,
                false,
                setUpMemberId,
                null
        );

        //when
        Long updatedSpaceId = spaceFacade.updateSpace(request);

        //then
        Space space = spaceJpaRepository.findById(updatedSpaceId).get();

        assertThat(space.getSpaceName()).isEqualTo("업데이트 스페이스 네임");
        assertThat(space.getDescription()).isEqualTo("업데이트 스페이스 소개글");
        assertThat(space.getCategory()).isEqualTo(Category.HOBBY_LEISURE_TRAVEL);
        assertThat(space.getIsVisible()).isEqualTo(false);
        assertThat(space.getIsComment()).isEqualTo(false);
        assertThat(space.getIsLinkSummarizable()).isEqualTo(false);
        assertThat(space.getIsReadMarkEnabled()).isEqualTo(false);
        assertThat(space.getSpaceImages().get(0).getPath()).isEqualTo("https://testimage1");
    }

    @Test
    @DisplayName("유저는 스페이스를 삭제할 수 있다.")
    void deleteSpace() {
        //when
        spaceFacade.deleteSpace(setUpSpaceId, setUpMemberId);

        //then
        Optional<Space> space = spaceJpaRepository.findById(setUpSpaceId);

        assertThat(space.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("스페이스의 주인이 아닌 유저가 스페이스를 삭제할 경우 UnauthorizedAccessException가 발생한다. ")
    void deleteSpace_UnauthorizedAccessException() {
        //when, then
        assertThatThrownBy(() -> spaceFacade.deleteSpace(setUpSpaceId, setUpMemberId + 1))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    private void setUpData() {
        Member member = new Member(
                "testSocialId",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "잠자는 사자의 콧털",
                "테스트용 소개글",
                "abc@gmail.com",
                true,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지"),
                new FavoriteCategory(Category.KNOWLEDGE_ISSUE_CAREER)
        );

        setUpMemberId = memberJpaRepository.save(member).getId();

        Space space = new Space(
                setUpMemberId,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(setUpMemberId, Role.OWNER),
                false,
                true,
                true,
                true
        );

        setUpSpaceId = spaceJpaRepository.save(space).getId();
    }

}
