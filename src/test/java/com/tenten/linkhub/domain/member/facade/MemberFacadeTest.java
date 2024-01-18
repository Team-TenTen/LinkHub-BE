package com.tenten.linkhub.domain.member.facade;

import com.tenten.linkhub.IntegrationApplicationTest;
import com.tenten.linkhub.domain.member.facade.dto.MemberSpacesFindByQueryFacadeResponses;
import com.tenten.linkhub.domain.member.facade.dto.MemberSpacesFindByQueryFacadeResponses.MemberSpacesFindByQueryFacadeResponse;
import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberFacadeTest extends IntegrationApplicationTest {

    @Autowired
    private MemberFacade memberFacade;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Member me;
    private Member teamMember;

    private Long myMemberId;
    private Long teamMemberId;

    private Space myPrivateSpace;
    private Space myPublicSpace;
    private Space teamMemberSpace;

    @BeforeEach
    void setUp() {
        setUpData();
    }

    @AfterEach
    void tearDown() {
        memberJpaRepository.deleteAll();
        spaceJpaRepository.deleteAll();
    }

    @Transactional
    @Test
    @DisplayName("유저는 자신의 프라이빗 스페이스를 포함한 자신의 모든 스페이스를 조회할 수 있다.")
    void findMemberSpacesByQuery_findMySpaces() {
        //given
        MemberSpacesFindRequest request = new MemberSpacesFindRequest(
                PageRequest.of(0, 10),
                null,
                null,
                myMemberId,
                myMemberId
        );

        //when
        MemberSpacesFindByQueryFacadeResponses responses = memberFacade.findMemberSpacesByQuery(request);

        //then
        List<MemberSpacesFindByQueryFacadeResponse> content = responses.responses().getContent();

        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).spaceName()).isEqualTo(myPrivateSpace.getSpaceName());
        assertThat(content.get(0).ownerNickName()).isEqualTo(me.getNickname());
        assertThat(content.get(1).spaceName()).isEqualTo(myPublicSpace.getSpaceName());
        assertThat(content.get(1).ownerNickName()).isEqualTo(me.getNickname());
    }

    @Transactional
    @Test
    @DisplayName("유저는 자신의 프라이빗 스페이스를 포함한 자신의 모든 스페이스를 필터 조회할 수 있다.")
    void findMemberSpacesByQuery_filter() {
        //given
        MemberSpacesFindRequest request = new MemberSpacesFindRequest(
                PageRequest.of(0, 10),
                null,
                Category.KNOWLEDGE_ISSUE_CAREER,
                myMemberId,
                myMemberId
        );

        //when
        MemberSpacesFindByQueryFacadeResponses responses = memberFacade.findMemberSpacesByQuery(request);

        //then
        List<MemberSpacesFindByQueryFacadeResponse> content = responses.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceName()).isEqualTo(myPublicSpace.getSpaceName());
        assertThat(content.get(0).ownerNickName()).isEqualTo(me.getNickname());
        assertThat(content.get(0).category()).isEqualTo(Category.KNOWLEDGE_ISSUE_CAREER);
    }

    @Transactional
    @Test
    @DisplayName("유저는 다른 유저의 프라이빗 스페이스를 제외한 스페이스를 조회할 수 있다.")
    void findMemberSpacesByQuery_findAnotherMemberSpaces() {
        //given
        MemberSpacesFindRequest request = new MemberSpacesFindRequest(
                PageRequest.of(0, 10),
                null,
                null,
                myMemberId + 5,
                myMemberId
        );

        //when
        MemberSpacesFindByQueryFacadeResponses responses = memberFacade.findMemberSpacesByQuery(request);

        //then
        List<MemberSpacesFindByQueryFacadeResponse> content = responses.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceName()).isEqualTo(myPublicSpace.getSpaceName());
        assertThat(content.get(0).ownerNickName()).isEqualTo(me.getNickname());
    }

    @Transactional
    @Test
    @DisplayName("유저는 다른 유저가 만든 프라이빗 스페이스에 스페이스 멤버면 자신의 스페이스 조회에 해당 스페이스가 조회된다.")
    void findMemberSpacesByQuery_findPrivateSpacesBySpaceMember() {
        //given
        MemberSpacesFindRequest request = new MemberSpacesFindRequest(
                PageRequest.of(0, 10),
                null,
                null,
                teamMemberId,
                teamMemberId
        );

        //when
        MemberSpacesFindByQueryFacadeResponses responses = memberFacade.findMemberSpacesByQuery(request);

        //then
        List<MemberSpacesFindByQueryFacadeResponse> content = responses.responses().getContent();

        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).spaceName()).isEqualTo(myPrivateSpace.getSpaceName());
        assertThat(content.get(0).ownerNickName()).isEqualTo(me.getNickname());
        assertThat(content.get(1).spaceName()).isEqualTo(teamMemberSpace.getSpaceName());
        assertThat(content.get(1).ownerNickName()).isEqualTo(teamMember.getNickname());
    }

    @Test
    @DisplayName("유저는 자신의 스페이스중 스페이스 이름을 검색할 수 있다.")
    void findMemberSpacesByQuery_searchMySpace() {
        //given
        MemberSpacesFindRequest request = new MemberSpacesFindRequest(
                PageRequest.of(0, 10),
                myPrivateSpace.getSpaceName(),
                null,
                myMemberId,
                myMemberId
        );

        //when
        MemberSpacesFindByQueryFacadeResponses responses = memberFacade.findMemberSpacesByQuery(request);

        //then
        List<MemberSpacesFindByQueryFacadeResponse> content = responses.responses().getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).spaceName()).isEqualTo(myPrivateSpace.getSpaceName());
        assertThat(content.get(0).ownerNickName()).isEqualTo(me.getNickname());
    }

    private void setUpData() {
        me = new Member(
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

        teamMember = new Member(
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

        myMemberId = memberJpaRepository.save(me).getId();
        teamMemberId = memberJpaRepository.save(teamMember).getId();
        Long setUpMemberId3 = memberJpaRepository.save(member3).getId();

        Space space = new Space(
                myMemberId,
                "나의 프라이빗 스페이스",
                "나의 프라이빗 스페이스 소개글",
                Category.ETC,
                new SpaceImage("https://testimage", "테스트 이미지"),
                new SpaceMember(myMemberId, Role.OWNER),
                false,
                true,
                true,
                true
        );

        Space space2 = new Space(
                myMemberId,
                "나의 퍼블릭 스페이스",
                "나의 퍼블릭 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(myMemberId, Role.OWNER),
                true,
                true,
                true,
                true
        );

        Space space3 = new Space(
                teamMemberId,
                "팀 멤버의 스페이스",
                "팀 멤버의 소개글",
                Category.ETC,
                new SpaceImage("https://testimage2", "테스트 이미지2"),
                new SpaceMember(teamMemberId, Role.OWNER),
                true,
                true,
                true,
                true
        );

        space.addSpaceMember(new SpaceMember(teamMemberId, Role.CAN_VIEW));
        space.addSpaceMember(new SpaceMember(setUpMemberId3, Role.CAN_EDIT));

        myPrivateSpace = spaceJpaRepository.save(space);
        myPublicSpace = spaceJpaRepository.save(space2);
        teamMemberSpace = spaceJpaRepository.save(space3);
    }
}