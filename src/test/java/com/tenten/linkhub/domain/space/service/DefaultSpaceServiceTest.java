package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.MemberJpaRepository;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.space.MySpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponse;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@TestPropertySource(locations = "classpath:/application-test.yml")
@SpringBootTest
class DefaultSpaceServiceTest {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Long setUpMemberId;

    @BeforeEach
    void setUp() {
        setupData();
    }

    @Test
    @DisplayName("유저는 올바른 키워드, 필터, 정렬 조건들을 통해 Space를 검색할 수 있다.")
    void findSpacesByQuery() {
        //given
        PageRequest pageRequest = PageRequest.of(
                0,
                10,
                Sort.by("created_at").descending());

        SpacesFindByQueryRequest request = new SpacesFindByQueryRequest(pageRequest,
                "첫번째",
                Category.KNOWLEDGE_ISSUE_CAREER);

        //when
        SpacesFindByQueryResponses responses = spaceService.findSpacesByQuery(request);

        //then
        List<SpacesFindByQueryResponse> content = responses.responses().getContent();

        assertThat(content.get(0).spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(content.get(0).description()).isEqualTo("첫번째 스페이스 소개글");
        assertThat(content.get(0).category()).isEqualTo(Category.KNOWLEDGE_ISSUE_CAREER);
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage1");
    }

    @Test
    @DisplayName("유저는 키워드와 필터 조건 없이 자신의 Space를 검색할 수 있다.")
    void findMySpacesByQuery_emptyKeyWord_emptyFilter() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        MySpacesFindRequest mySpacesFindRequest = new MySpacesFindRequest(pageRequest, "", null, setUpMemberId);

        //when
        SpacesFindByQueryResponses response = spaceService.findMySpacesByQuery(mySpacesFindRequest);

        //then
        List<SpacesFindByQueryResponse> content = response.responses().getContent();

        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(content.get(0).description()).isEqualTo("첫번째 스페이스 소개글");
        assertThat(content.get(0).category()).isEqualTo(Category.KNOWLEDGE_ISSUE_CAREER);
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage1");
        assertThat(content.get(1).spaceName()).isEqualTo("두번째 스페이스");
    }

    @Test
    @DisplayName("유저는 키워드를 통해 자신의 Space를 검색할 수 있다.")
    void findMySpacesByQuery_keyWord_emptyFilter() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        MySpacesFindRequest mySpacesFindRequest = new MySpacesFindRequest(pageRequest, "두번째", null, setUpMemberId);

        //when
        SpacesFindByQueryResponses response = spaceService.findMySpacesByQuery(mySpacesFindRequest);

        //then
        List<SpacesFindByQueryResponse> content = response.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceName()).isEqualTo("두번째 스페이스");
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage2");
    }

    @Test
    @DisplayName("유저는 필터 조건을 통해 자신의 Space를 검색할 수 있다.")
    void findMySpacesByQuery_emptyKeyWord_filter() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        MySpacesFindRequest mySpacesFindRequest = new MySpacesFindRequest(pageRequest, "", Category.LIFE_KNOWHOW_SHOPPING, setUpMemberId);

        //when
        SpacesFindByQueryResponses response = spaceService.findMySpacesByQuery(mySpacesFindRequest);

        //then
        List<SpacesFindByQueryResponse> content = response.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceName()).isEqualTo("두번째 스페이스");
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage2");
    }

    private void setupData() {
        Member member = new Member(
                "testSocialId",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "잠자는 사자의 콧털",
                "테스트용 소개글",
                "abc@gmail.com",
                false,
                new ProfileImage("https://testimage1", "테스트 이미지1"),
                new FavoriteCategory(Category.ENTER_ART)
        );

        member.addProfileImage(
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지")
        );

        setUpMemberId = memberJpaRepository.save(member).getId();

        Space space1 = new Space(
                setUpMemberId,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                true,
                true,
                true
        );

        Space space2 = new Space(
                setUpMemberId,
                "두번째 스페이스",
                "두번째 스페이스 소개글",
                Category.LIFE_KNOWHOW_SHOPPING,
                true,
                true,
                true,
                true
        );

        Space space3 = new Space(
                setUpMemberId + 1,
                "세번째 스페이스",
                "세번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                true,
                true,
                true
        );

        space1.addSpaceMember(
                new SpaceMember(setUpMemberId, Role.OWNER)
        );

        space2.addSpaceMember(
                new SpaceMember(setUpMemberId, Role.OWNER)
        );

        space3.addSpaceMember(
                new SpaceMember(setUpMemberId + 1, Role.OWNER)
        );

        space1.addSpaceImage(
                new SpaceImage("https://testimage1", "테스트 이미지1")
        );

        space2.addSpaceImage(
                new SpaceImage("https://testimage2", "테스트 이미지2")
        );

        space3.addSpaceImage(
                new SpaceImage("https://testimage3", "테스트 이미지3")
        );

        spaceJpaRepository.save(space1);
        spaceJpaRepository.save(space2);
        spaceJpaRepository.save(space3);
    }

}
