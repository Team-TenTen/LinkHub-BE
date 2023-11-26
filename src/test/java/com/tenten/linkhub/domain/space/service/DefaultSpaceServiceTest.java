package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.exception.LinkViewHistoryException;
import com.tenten.linkhub.domain.space.facade.LinkFacade;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponses;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponse;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.dto.spacemember.SpaceMemberRoleChangeRequest;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    private Long myMemberId;
    private Long anotherMemberId;

    private Long myFirstSpaceId;
    private Long mySecondSpaceId;

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
        SpacesFindByQueryResponses responses = spaceService.findPublicSpacesByQuery(request);

        //then
        List<SpacesFindByQueryResponse> content = responses.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(content.get(0).description()).isEqualTo("첫번째 스페이스 소개글");
        assertThat(content.get(0).category()).isEqualTo(Category.KNOWLEDGE_ISSUE_CAREER);
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage1");
        assertThat(content.get(0).ownerNickName()).isEqualTo("잠자는 사자의 콧털");
    }

    @Test
    @DisplayName("유저는 키워드 필터 조건 없이 내가 아닌 특정 유저의 private을 제외한 public Space를 검색할 수 있다.")
    void findMemberSpacesByQuery_emptyKeyWord_emptyFilter() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        MemberSpacesFindRequest memberSpacesFindRequest = new MemberSpacesFindRequest(pageRequest, null, null, myMemberId, anotherMemberId);

        //when
        SpacesFindByQueryResponses response = spaceService.findMemberSpacesByQuery(memberSpacesFindRequest);

        //then
        List<SpacesFindByQueryResponse> content = response.responses().getContent();

        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(1).spaceName()).isEqualTo("세번째 스페이스");
        assertThat(content.get(1).spaceImagePath()).isEqualTo("https://testimage3");
        assertThat(content.get(1).ownerNickName()).isEqualTo("백둥이");
    }

    @Test
    @DisplayName("유저는 키워드와 필터 조건 없이 자신의 Space를 검색할 수 있다.")
    void findMemberSpacesByQuery_emptyKeyWord_emptyFilter_findMySpaces() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        MemberSpacesFindRequest memberSpacesFindRequest = new MemberSpacesFindRequest(pageRequest, null, null, myMemberId, myMemberId);

        //when
        SpacesFindByQueryResponses response = spaceService.findMemberSpacesByQuery(memberSpacesFindRequest);

        //then
        List<SpacesFindByQueryResponse> content = response.responses().getContent();

        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(content.get(0).description()).isEqualTo("첫번째 스페이스 소개글");
        assertThat(content.get(0).category()).isEqualTo(Category.KNOWLEDGE_ISSUE_CAREER);
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage1");
        assertThat(content.get(0).ownerNickName()).isEqualTo("잠자는 사자의 콧털");
        assertThat(content.get(1).spaceName()).isEqualTo("두번째 스페이스");
        assertThat(content.get(1).ownerNickName()).isEqualTo("잠자는 사자의 콧털");
    }

    @Test
    @DisplayName("유저는 키워드를 통해 자신의 Space를 검색할 수 있다.")
    void findMemberSpacesByQuery_keyWord_emptyFilter_findMySpaces() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        MemberSpacesFindRequest memberSpacesFindRequest = new MemberSpacesFindRequest(pageRequest, "두번째", null, myMemberId, myMemberId);

        //when
        SpacesFindByQueryResponses response = spaceService.findMemberSpacesByQuery(memberSpacesFindRequest);

        //then
        List<SpacesFindByQueryResponse> content = response.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceName()).isEqualTo("두번째 스페이스");
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage2");
        assertThat(content.get(0).ownerNickName()).isEqualTo("잠자는 사자의 콧털");
    }

    @Test
    @DisplayName("유저는 필터 조건을 통해 자신의 Space를 검색할 수 있다.")
    void findMemberSpacesByQuery_emptyKeyWord_filter_findMySpaces() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        MemberSpacesFindRequest memberSpacesFindRequest = new MemberSpacesFindRequest(pageRequest, "", Category.LIFE_KNOWHOW_SHOPPING, myMemberId, myMemberId);

        //when
        SpacesFindByQueryResponses response = spaceService.findMemberSpacesByQuery(memberSpacesFindRequest);

        //then
        List<SpacesFindByQueryResponse> content = response.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceName()).isEqualTo("두번째 스페이스");
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage2");
        assertThat(content.get(0).ownerNickName()).isEqualTo("잠자는 사자의 콧털");
    }

    @Test
    @DisplayName("유저는 스페이스에서 사용된 태그 목록을 확인할 수 있다. - 링크 생성")
    void getTagsBySpaceId_spaceId_Success() {
        //given - 링크 생성 3개 생성 그 중 2개는 태그명이 같다.
        Space space = spaceJpaRepository.findById(myFirstSpaceId).get();
        linkFacade.createLink(myFirstSpaceId, myMemberId, new LinkCreateFacadeRequest("https://www.naver.com", "제목A", "태그1", "gray"));
        linkFacade.createLink(myFirstSpaceId, myMemberId, new LinkCreateFacadeRequest("https://www.naver.com", "제목B", "태그1", "gray"));
        linkFacade.createLink(myFirstSpaceId, myMemberId, new LinkCreateFacadeRequest("https://www.naver.com", "제목C", "태그2", "red"));

        //when
        SpaceTagGetResponses response = spaceService.getTagsBySpaceId(myFirstSpaceId);

        //then
        assertThat(response.tags()).hasSize(2);
    }

    @Test
    @DisplayName("스페이스에서 사용된 태그 목록을 조회 시 더이상 스페이스 내에서 사용하지 않는 태그는 조회되지 않는다.")
    void getTagsBySpaceId_spaceId2_Success() {
        //given - 링크 2개를 저장 후 그 중 하나를 삭제
        Space space = spaceJpaRepository.findById(myFirstSpaceId).get();
        Long linkId1 = linkFacade.createLink(myFirstSpaceId, myMemberId, new LinkCreateFacadeRequest("https://www.naver.com", "제목A", "태그1", "gray"));
        Long linkId2 = linkFacade.createLink(myFirstSpaceId, myMemberId, new LinkCreateFacadeRequest("https://www.naver.com", "제목C", "태그2", "red"));
        linkFacade.deleteLink(space.getId(), linkId2, myMemberId);

        //when
        SpaceTagGetResponses response = spaceService.getTagsBySpaceId(myFirstSpaceId);

        //then
        assertThat(response.tags()).hasSize(1);
        assertThat(response.tags().get(0).name()).isEqualTo("태그1");
    }

    @Test
    @DisplayName("스페이스에서 읽음 처리 기능을 활성화하지 않았다면 이력을 저장할 수 없다.")
    void checkLinkViewHistory_MemberIdAndSpaceId_ThrowsException() {
        //when & then
        assertThatThrownBy(() -> spaceService.checkLinkViewHistory(myFirstSpaceId, myMemberId))
                .isInstanceOf(LinkViewHistoryException.class);
    }

    @Test
    @DisplayName("스페이스의 멤버가 아니라면 읽음 처리 기능이 활성화 되어있더라도 이력을 저장할 수 없다.")
    void checkLinkViewHistory_MemberIdAndSpaceId2_ThrowsException() {
        //when & then
        assertThatThrownBy(() -> spaceService.checkLinkViewHistory(mySecondSpaceId, myMemberId))
                .isInstanceOf(LinkViewHistoryException.class);
    }

    @Test
    @DisplayName("스페이스의 오너는 스페이스 멤버들의 권한을 변경할 수 있다.")
    void changeSpaceMembersRole() {
        //given
        SpaceMemberRoleChangeRequest request = new SpaceMemberRoleChangeRequest(myFirstSpaceId, myMemberId, anotherMemberId, Role.CAN_EDIT);

        //when
        Long spaceId = spaceService.changeSpaceMembersRole(request);

        //then
        Space space = spaceJpaRepository.findById(spaceId).get();

        List<SpaceMember> spaceMembers = space.getSortedSpaceMember();
        assertThat(spaceMembers.size()).isEqualTo(2);
        assertThat(spaceMembers.get(1).getRole()).isEqualTo(Role.CAN_EDIT);
    }

    @Test
    @DisplayName("스페이스의 오너가 아닌 멤버가 스페이스 멤버의 권한을 변경하려고 하면 UnauthorizedAccessException예외가 발생한다.")
    void changeSpaceMembersRole_UnauthorizedAccessException() {
        //given
        SpaceMemberRoleChangeRequest request = new SpaceMemberRoleChangeRequest(myFirstSpaceId, anotherMemberId, anotherMemberId, Role.CAN_EDIT);

        //when//then
        assertThatThrownBy(() -> spaceService.changeSpaceMembersRole(request))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("")
    void deleteSpaceMemberByMe() {
        //when
        spaceService.deleteSpaceMemberByMe(myFirstSpaceId, anotherMemberId);

        //then
        Space space = spaceJpaRepository.findById(myFirstSpaceId).get();
        List<SpaceMember> spaceMembers = space.getSpaceMembers();

        assertThat(spaceMembers.size()).isEqualTo(1);
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

        Member member1 = new Member(
                "testSocialId",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "백둥이",
                "백둥이 테스트용 소개글",
                "abc@gmail.com",
                true,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지"),
                new FavoriteCategory(Category.KNOWLEDGE_ISSUE_CAREER)
        );

        myMemberId = memberJpaRepository.save(member).getId();
        anotherMemberId = memberJpaRepository.save(member1).getId();

        Space space1 = new Space(
                myMemberId,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(myMemberId, Role.OWNER),
                true,
                true,
                true,
                false
        );

        space1.addSpaceImage(
                new SpaceImage("https://testimage4", "테스트 이미지4")
        );

        space1.addSpaceMember(
                new SpaceMember(anotherMemberId, Role.CAN_VIEW)
        );

        Space space2 = new Space(
                myMemberId,
                "두번째 스페이스",
                "두번째 스페이스 소개글",
                Category.LIFE_KNOWHOW_SHOPPING,
                new SpaceImage("https://testimage2", "테스트 이미지2"),
                new SpaceMember(myMemberId, Role.OWNER),
                true,
                true,
                true,
                true
        );

        Space space3 = new Space(
                anotherMemberId,
                "세번째 스페이스",
                "세번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage3", "테스트 이미지3"),
                new SpaceMember(anotherMemberId, Role.OWNER),
                true,
                true,
                true,
                true
        );

        Space space4 = new Space(
                anotherMemberId,
                "네번째 스페이스",
                "네번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage4", "테스트 이미지4"),
                new SpaceMember(anotherMemberId, Role.OWNER),
                false,
                false,
                false,
                false
        );

        spaceJpaRepository.save(space1);
        spaceJpaRepository.save(space2);
        spaceJpaRepository.save(space3);
        spaceJpaRepository.save(space4);

        myFirstSpaceId = space1.getId();
        mySecondSpaceId = space3.getId();
    }

}
