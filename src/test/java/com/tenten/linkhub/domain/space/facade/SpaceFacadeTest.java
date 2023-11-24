package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.facade.dto.NewSpacesScrapFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.facade.dto.SpaceMemberDetailInfo;
import com.tenten.linkhub.domain.space.facade.dto.SpaceUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.model.space.Favorite;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.favorite.FavoriteJpaRepository;
import com.tenten.linkhub.domain.space.repository.link.LinkJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.LinkService;
import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.s3.ImageFileUploader;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class SpaceFacadeTest {

    @Autowired
    private SpaceFacade spaceFacade;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private FavoriteJpaRepository favoriteJpaRepository;

    @Autowired
    private LinkService linkService;

    @Autowired
    private LinkJpaRepository linkJpaRepository;

    @MockBean
    private ImageFileUploader mockImageFileUploader;

    private Long myMemberId;
    private Long anotherMemberId;

    private Long mySpaceId;
    private Long anotherSpaceId;

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
        BDDMockito.given(mockImageFileUploader.saveImage(any())).willReturn(imageInfo);

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
                mySpaceId,
                myMemberId,
                new ArrayList<>());

        //when
        SpaceDetailGetByIdFacadeResponse response = spaceFacade.getSpaceDetailById(request);

        //then
        List<SpaceMemberDetailInfo> spaceMemberDetailInfos = response.memberDetailInfos();

        assertThat(response.spaceId()).isEqualTo(mySpaceId);
        assertThat(response.spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(response.description()).isEqualTo("첫번째 스페이스 소개글");
        assertThat(response.spaceImagePath()).isEqualTo("https://testimage1");
        assertThat(response.viewCount()).isEqualTo(0L);
        assertThat(response.isOwner()).isEqualTo(true);
        assertThat(response.isCanEdit()).isEqualTo(true);
        assertThat(response.hasFavorite()).isEqualTo(true);
        assertThat(spaceMemberDetailInfos.get(0).memberId()).isEqualTo(myMemberId);
        assertThat(spaceMemberDetailInfos.get(0).nickname()).isEqualTo("잠자는 사자의 콧털");
        assertThat(spaceMemberDetailInfos.get(0).profilePath()).isEqualTo("https://testprofileimage");
    }

    @Test
    @DisplayName("프라이빗 스페이스의 상세 조회 시 권한이 없는 유저는 UnauthorizedAccessException가 발생한다.")
    void getSpaceDetailById_UnauthorizedAccessException() {
        //given
        SpaceDetailGetByIdFacadeRequest request = new SpaceDetailGetByIdFacadeRequest(
                mySpaceId,
                myMemberId + 100,
                new ArrayList<>());

        //when//then
        assertThatThrownBy(() -> spaceFacade.getSpaceDetailById(request))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("스페이스 상세 정보 조회 시 스페이스 멤버들의 정보는 정렬되어 반환 받는다.")
    void getSpaceDetailById_orderTest() {
        //given
        SpaceDetailGetByIdFacadeRequest request = new SpaceDetailGetByIdFacadeRequest(
                mySpaceId,
                myMemberId,
                new ArrayList<>());

        //when
        SpaceDetailGetByIdFacadeResponse response = spaceFacade.getSpaceDetailById(request);

        //then
        List<SpaceMemberDetailInfo> spaceMemberDetailInfos = response.memberDetailInfos();

        assertThat(response.spaceId()).isEqualTo(mySpaceId);
        assertThat(response.spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(spaceMemberDetailInfos.get(0).nickname()).isEqualTo("잠자는 사자의 콧털");
        assertThat(spaceMemberDetailInfos.get(0).SpaceMemberRole()).isEqualTo(Role.OWNER);
        assertThat(spaceMemberDetailInfos.get(1).nickname()).isEqualTo("백둥이");
        assertThat(spaceMemberDetailInfos.get(1).SpaceMemberRole()).isEqualTo(Role.CAN_EDIT);
        assertThat(spaceMemberDetailInfos.get(2).nickname()).isEqualTo("프롱이");
        assertThat(spaceMemberDetailInfos.get(2).SpaceMemberRole()).isEqualTo(Role.CAN_VIEW);
    }

    @Test
    @DisplayName("유저는 스페이스 이미지를 포함한 스페이스의 정보들을 변경할 수 있다.")
    void updateSpace() {
        //given
        MockMultipartFile requestFile = new MockMultipartFile("업데이트된 이미지 파일 이름", (byte[]) null);
        ImageInfo imageInfo = ImageInfo.of("https://updateimage", requestFile.getName());
        BDDMockito.given(mockImageFileUploader.saveImage(any())).willReturn(imageInfo);

        SpaceUpdateFacadeRequest request = new SpaceUpdateFacadeRequest(
                mySpaceId,
                "업데이트 스페이스 네임",
                "업데이트 스페이스 소개글",
                Category.HOBBY_LEISURE_TRAVEL,
                false,
                false,
                false,
                false,
                myMemberId,
                requestFile
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
        assertThat(space.getSpaceImages().get(0).getPath()).isEqualTo("https://updateimage");
    }

    @Test
    @DisplayName("유저는 스페이스 이미지를 제외한 스페이스의 정보를 변경할 수 있다.")
    void updateSpace_emptySpaceImage() {
        //given
        ImageInfo imageInfo = ImageInfo.of("https://testimage3", "테스트 이미지 파일 이름");
        BDDMockito.given(mockImageFileUploader.saveImage(any())).willReturn(imageInfo);

        SpaceUpdateFacadeRequest request = new SpaceUpdateFacadeRequest(
                mySpaceId,
                "업데이트 스페이스 네임",
                "업데이트 스페이스 소개글",
                Category.HOBBY_LEISURE_TRAVEL,
                false,
                false,
                false,
                false,
                myMemberId,
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
        spaceFacade.deleteSpace(mySpaceId, myMemberId);

        //then
        Optional<Space> space = spaceJpaRepository.findById(mySpaceId);

        assertThat(space.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("스페이스의 주인이 아닌 유저가 스페이스를 삭제할 경우 UnauthorizedAccessException가 발생한다. ")
    void deleteSpace_UnauthorizedAccessException() {
        //when, then
        assertThatThrownBy(() -> spaceFacade.deleteSpace(mySpaceId, myMemberId + 1))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("유저는 다른 유저의 스페이스를 복사하여 자신의 새로운 스페이스로 생성할 수 있다.")
    void scrapAndCreateNewSpace() {
        //given
        NewSpacesScrapFacadeRequest request = new NewSpacesScrapFacadeRequest(
                "가져오기한 스페이스",
                "다른 유저의 스페이스를 가져오기한 스페이스 입니다.",
                Category.ETC,
                true,
                true,
                true,
                true,
                null,
                anotherSpaceId,
                myMemberId
        );

        //when
        Long savedSpaceId = spaceFacade.scrapAndCreateNewSpace(request);

        //then
        List<Link> scrapedLinks = linkJpaRepository.findBySpaceIdAndIsDeletedFalse(savedSpaceId);

        assertThat(scrapedLinks.get(0).getTitle()).isEqualTo("링크허브 사이트");
        assertThat(scrapedLinks.get(0).getLinkTags().get(0).getTag().getName()).isEqualTo("도구");
        assertThat(scrapedLinks.get(1).getTitle()).isEqualTo("무신사");
        assertThat(scrapedLinks.get(1).getLinkTags().get(0).getTag().getName()).isEqualTo("의류");
    }

    private void setUpData() {
        Member member1 = new Member(
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

        Member member2 = new Member(
                "testSocialId",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "프롱이",
                "프롱이 소개글",
                "abc@gmail.com",
                true,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지"),
                new FavoriteCategory(Category.KNOWLEDGE_ISSUE_CAREER)
        );

        Member member3 = new Member(
                "testSocialId",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "백둥이",
                "백둥이 소개글",
                "abc@gmail.com",
                true,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지"),
                new FavoriteCategory(Category.KNOWLEDGE_ISSUE_CAREER)
        );

        myMemberId = memberJpaRepository.save(member1).getId();
        anotherMemberId = memberJpaRepository.save(member2).getId();
        Long setUpMemberId3 = memberJpaRepository.save(member3).getId();

        Space space = new Space(
                myMemberId,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(myMemberId, Role.OWNER),
                false,
                true,
                true,
                true
        );

        Space space2 = new Space(
                anotherMemberId,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(anotherMemberId, Role.OWNER),
                true,
                true,
                true,
                true
        );

        space.addSpaceMember(new SpaceMember(anotherMemberId, Role.CAN_VIEW));
        space.addSpaceMember(new SpaceMember(setUpMemberId3, Role.CAN_EDIT));

        Space savedSpace = spaceJpaRepository.save(space);
        mySpaceId = savedSpace.getId();
        anotherSpaceId = spaceJpaRepository.save(space2).getId();

        Favorite favorite = new Favorite(savedSpace, myMemberId);
        favoriteJpaRepository.save(favorite);

        linkService.createLink(
                new LinkCreateRequest(
                        anotherSpaceId,
                        "https://link-hub.site/1",
                        "링크허브 사이트",
                        "도구",
                        anotherMemberId,
                        "yellow"
                )
        );

        linkService.createLink(
                new LinkCreateRequest(
                        anotherSpaceId,
                        "https://musinsa.com",
                        "무신사",
                        "의류",
                        anotherMemberId,
                        "blue"
                )
        );
    }

}
