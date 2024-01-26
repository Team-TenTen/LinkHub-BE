package com.tenten.linkhub.domain.link.service;

import com.tenten.linkhub.IntegrationApplicationTest;
import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.link.facade.LinkFacade;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.link.model.Link;
import com.tenten.linkhub.domain.link.model.vo.Url;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.link.repository.link.LinkJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Sql(scripts = "/sql/clean_up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class LikeServiceTest extends IntegrationApplicationTest {
    private static int THREAD_COUNT = 100;

    @Autowired
    private LinkFacade linkFacade;

    @Autowired
    private LinkJpaRepository linkJpaRepository;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Long setUpMemberId1;
    private Long setUpLinkId1;
    private List<Member> members;

    @BeforeEach
    void setUp() {
        setUpData();
    }

    @Test
    @DisplayName("동시에 링크에 좋아요를 누르더라도 좋아요 수가 정상적으로 수정된다.")
    void createFavorite_concurrencyTest() throws InterruptedException {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        //when
        for (int i = 0; i < THREAD_COUNT; i++) {
            int memberAddNumber = i;
            executorService.submit(() -> {
                try {
                    linkFacade.createLike(setUpLinkId1, setUpMemberId1 + memberAddNumber);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        Thread.sleep(1000);

        //then
        Link link = linkJpaRepository.findById(setUpLinkId1).get();
        assertThat(link.getLikeCount()).isEqualTo(THREAD_COUNT);
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

        setUpMemberId1 = memberJpaRepository.save(member1).getId();

        Space space1 = new Space(
                setUpMemberId1,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(setUpMemberId1, Role.OWNER),
                true,
                true,
                true,
                true
        );

        space1.addSpaceImage(
                new SpaceImage("https://testimage2", "테스트 이미지2")
        );

        spaceJpaRepository.save(space1);

        Link link1 = Link.toLink(space1, setUpMemberId1, "링크의 제목1", new Url("https://www.naver.com"));

        setUpLinkId1 = linkJpaRepository.save(link1).getId();
    }


}
