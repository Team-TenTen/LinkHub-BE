package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.repository.MemberJpaRepository;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.facade.dto.SpaceMemberDetailInfo;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


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

    private Long setUpSpaceId;
    private Long setUpMemberId;

    @BeforeEach
    void setUp(){
        setUpData();
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