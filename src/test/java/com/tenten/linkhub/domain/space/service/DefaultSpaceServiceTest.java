package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.facade.LinkFacade;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.link.Color;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.space.MySpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagsGetResponse;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponse;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class DefaultSpaceServiceTest {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private LinkFacade linkFacade;

    private Long setUpMemberId;
    private Long spaceId1;

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

        PublicSpacesFindByQueryRequest request = new PublicSpacesFindByQueryRequest(pageRequest,
                "첫번째",
                Category.KNOWLEDGE_ISSUE_CAREER);

        //when
        PublicSpacesFindByQueryResponses responses = spaceService.findPublicSpacesByQuery(request);

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
        PublicSpacesFindByQueryResponses response = spaceService.findMySpacesByQuery(mySpacesFindRequest);

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
        PublicSpacesFindByQueryResponses response = spaceService.findMySpacesByQuery(mySpacesFindRequest);

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
        PublicSpacesFindByQueryResponses response = spaceService.findMySpacesByQuery(mySpacesFindRequest);

        //then
        List<SpacesFindByQueryResponse> content = response.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceName()).isEqualTo("두번째 스페이스");
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage2");
    }

    @Test
    @DisplayName("유저는 스페이스에서 사용된 태그 목록을 확인할 수 있다. - 링크 생성")
    void getTagsBySpaceId_spaceId_Success() {
        //given - 링크 생성 3개 생성 그 중 2개는 태그명이 같다.
        Space space = spaceJpaRepository.findById(spaceId1).get();
        linkFacade.createLink(spaceId1, setUpMemberId, new LinkCreateFacadeRequest("https://www.naver.com", "제목A", "태그1", Color.BLUE));
        linkFacade.createLink(spaceId1, setUpMemberId, new LinkCreateFacadeRequest("https://www.naver.com", "제목B", "태그1", Color.GRAY));
        linkFacade.createLink(spaceId1, setUpMemberId, new LinkCreateFacadeRequest("https://www.naver.com", "제목C", "태그2", Color.RED));

        //when
        SpaceTagsGetResponse response = spaceService.getTagsBySpaceId(spaceId1);

        //then
        assertThat(response.tagNames()).hasSize(2);
        assertThat(response.tagNames()).containsExactlyInAnyOrderElementsOf(List.of("태그1", "태그2"));
    }

    private void setupData() {
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

        Space space1 = new Space(
                setUpMemberId,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(setUpMemberId, Role.OWNER),
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
                new SpaceImage("https://testimage2", "테스트 이미지2"),
                new SpaceMember(setUpMemberId, Role.OWNER),
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
                new SpaceImage("https://testimage3", "테스트 이미지3"),
                new SpaceMember(setUpMemberId + 1, Role.OWNER),
                true,
                true,
                true,
                true
        );

        spaceJpaRepository.save(space1);
        spaceJpaRepository.save(space2);
        spaceJpaRepository.save(space3);

        spaceId1 = space1.getId();
    }

}
