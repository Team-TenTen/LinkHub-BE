package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Favorite;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.favorite.FavoriteJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.repository.spacemember.SpaceMemberJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.favorite.FavoriteSpacesFindResponse;
import com.tenten.linkhub.domain.space.service.dto.favorite.FavoriteSpacesFindResponses;
import com.tenten.linkhub.domain.space.service.dto.favorite.MyFavoriteSpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.favorite.SpaceRegisterInFavoriteResponse;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteJpaRepository favoriteJpaRepository;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Long setUpMemberId1;
    private Long setUpSpaceId1;
    private Long setUpSpaceId2;

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @AfterEach
    void tearDown() {
        favoriteJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("유저는 스페이스를 즐겨찾기에 등록할 수 있다.")
    void createFavorite() throws InterruptedException {
        //when
        SpaceRegisterInFavoriteResponse response = favoriteService.createFavorite(setUpSpaceId1, setUpMemberId1);
        Thread.sleep(100);

        //then
        Favorite savedFavorite = favoriteJpaRepository.findById(response.favoriteId()).get();
        Space space = spaceJpaRepository.findById(setUpSpaceId1).get();

        assertThat(savedFavorite.getMemberId()).isEqualTo(setUpMemberId1);
        assertThat(space.getSpaceName()).isEqualTo("첫번째 스페이스");
        assertThat(space.getFavoriteCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("다수의 유저가 동시에 즐겨찾기 등록을 진행해도 Space의 viewCount의 정합성을 보장한다.")
    void createFavorite_concurrencyTest() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            int memberAddNumber = i;
            executorService.submit(() -> {
                try {
                    SpaceRegisterInFavoriteResponse response = favoriteService.createFavorite(setUpSpaceId1, setUpMemberId1 + memberAddNumber);
                    favoriteJpaRepository.findById(response.favoriteId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Thread.sleep(1000);

        //then
        Space space = spaceJpaRepository.findById(setUpSpaceId1).get();
        assertThat(space.getFavoriteCount()).isEqualTo(100L);
    }

    @Test
    @DisplayName("유저는 스페이스 즐겨찾기를 취소할 수 있다.")
    void cancelFavoriteSpace() throws InterruptedException {
        //given
        favoriteService.createFavorite(setUpSpaceId1, setUpMemberId1);
        Thread.sleep(100);

        //when
        Long deletedFavoriteId = favoriteService.cancelFavoriteSpace(setUpSpaceId1, setUpMemberId1);
        Thread.sleep(100);

        //then
        Space space = spaceJpaRepository.findById(setUpSpaceId1).get();

        assertThat(space.getFavoriteCount()).isEqualTo(0L);
        assertThatThrownBy(() -> {
            favoriteJpaRepository.findById(deletedFavoriteId)
                    .orElseThrow(() -> new DataNotFoundException("해당 Favorite은 존재하지 않습니다."));
        })
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @DisplayName("유저는 자신이 즐겨찾기 등록한 스페이스를 키워드, 필터 조건을 통해 조회할 수 있다.")
    void findMyFavoriteSpaces() {
        //given
        favoriteService.createFavorite(setUpSpaceId1, setUpMemberId1);
        favoriteService.createFavorite(setUpSpaceId2, setUpMemberId1);

        PageRequest pageRequest = PageRequest.of(0, 10);
        MyFavoriteSpacesFindRequest request = new MyFavoriteSpacesFindRequest(pageRequest, "첫번째", null, setUpMemberId1);

        //when
        FavoriteSpacesFindResponses serviceResponses = favoriteService.findMyFavoriteSpaces(request);

        //then
        List<FavoriteSpacesFindResponse> content = serviceResponses.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceId()).isEqualTo(setUpSpaceId1);
        assertThat(content.get(0).spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(content.get(0).ownerNickName()).isEqualTo("백둥이");
        assertThat(content.get(0).favoriteCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유저는 자신이 즐겨찾기 등록한 스페이스를 키워드 조건 없이 필터 조건을 통해 조회할 수 있다.")
    void findMyFavoriteSpaces_emptyKeyWord() {
        //given
        favoriteService.createFavorite(setUpSpaceId1, setUpMemberId1);
        favoriteService.createFavorite(setUpSpaceId2, setUpMemberId1);

        PageRequest pageRequest = PageRequest.of(0, 10);
        MyFavoriteSpacesFindRequest request = new MyFavoriteSpacesFindRequest(pageRequest, null, Category.KNOWLEDGE_ISSUE_CAREER, setUpMemberId1);

        //when
        FavoriteSpacesFindResponses serviceResponses = favoriteService.findMyFavoriteSpaces(request);

        //then
        List<FavoriteSpacesFindResponse> content = serviceResponses.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceId()).isEqualTo(setUpSpaceId1);
        assertThat(content.get(0).spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(content.get(0).ownerNickName()).isEqualTo("백둥이");
        assertThat(content.get(0).favoriteCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유저는 자신이 즐겨찾기 등록한 스페이스를 키워드, 필터 조건 없이 조회할 수 있다.")
    void findMyFavoriteSpaces_emptyKeyWord_emptyFilter() throws InterruptedException {
        //given
        favoriteService.createFavorite(setUpSpaceId1, setUpMemberId1);
        favoriteService.createFavorite(setUpSpaceId2, setUpMemberId1);
        Thread.sleep(10);

        PageRequest pageRequest = PageRequest.of(0, 10);
        MyFavoriteSpacesFindRequest request = new MyFavoriteSpacesFindRequest(pageRequest, null, null, setUpMemberId1);

        //when
        FavoriteSpacesFindResponses serviceResponses = favoriteService.findMyFavoriteSpaces(request);

        //then
        List<FavoriteSpacesFindResponse> content = serviceResponses.responses().getContent();

        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).spaceId()).isEqualTo(setUpSpaceId1);
        assertThat(content.get(0).spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(content.get(0).ownerNickName()).isEqualTo("백둥이");
        assertThat(content.get(0).favoriteCount()).isEqualTo(1L);
        assertThat(content.get(1).spaceId()).isEqualTo(setUpSpaceId2);
        assertThat(content.get(1).spaceName()).isEqualTo("두번째 스페이스");
        assertThat(content.get(1).ownerNickName()).isEqualTo("백둥이");
        assertThat(content.get(1).favoriteCount()).isEqualTo(1L);
    }

    private void setUpTestData() {
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
                "백둥이",
                "백둥이 소개글",
                "abc@gmail.com",
                true,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지"),
                new FavoriteCategory(Category.KNOWLEDGE_ISSUE_CAREER)
        );

        setUpMemberId1 = memberJpaRepository.save(member1).getId();
        Long seUpMemberId2 = memberJpaRepository.save(member2).getId();

        Space space1 = new Space(
                seUpMemberId2,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(seUpMemberId2, Role.OWNER),
                true,
                true,
                true,
                true
        );

        Space space2 = new Space(
                seUpMemberId2,
                "두번째 스페이스",
                "두번째 스페이스 소개글",
                Category.LIFE_KNOWHOW_SHOPPING,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(seUpMemberId2, Role.OWNER),
                true,
                true,
                true,
                true
        );

        setUpSpaceId1 = spaceJpaRepository.save(space1).getId();
        setUpSpaceId2 = spaceJpaRepository.save(space2).getId();
    }

}
