package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.link.LinkJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.repository.spacemember.SpaceMemberJpaRepository;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class LinkFacadeTest {

    @Autowired
    private LinkFacade linkFacade;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private SpaceMemberJpaRepository spaceMemberJpaRepository;

    @Autowired
    private LinkJpaRepository linkJpaRepository;

    private Long memberId1;
    private Long memberId2;
    private Long spaceId;

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @Test
    @DisplayName("사용자는 CAN_EDIT이나 OWNER 권한을 가진 경우 링크를 생성할 수 있다.")
    void createLink_request_Success() {
        //given
        LinkCreateFacadeRequest request = new LinkCreateFacadeRequest(
                "https://naver.com",
                "링크의 제목",
                "태그의 이름"
        );

        //when
        Long linkId = linkFacade.createLink(spaceId, memberId1, request);

        //then
        Link link = linkJpaRepository.findById(linkId).get();
        assertThat(link.getUrl().getUrl()).isEqualTo("https://naver.com");
        assertThat(link.getTitle()).isEqualTo("링크의 제목");
        assertThat(link.getTags().get(0).getName()).isEqualTo("태그의 이름");
    }

    @Test
    @DisplayName("사용자는 CAN_EDIT이나 OWNER 권한이 아닌 경우 링크를 생성할 수 없다.")
    void createLink_request_ThrowsUnauthorizedAccessException() {
        //given
        LinkCreateFacadeRequest request = new LinkCreateFacadeRequest(
                "https://naver.com",
                "링크의 제목",
                "태그의 이름"
        );

        //when
        Assertions.assertThatThrownBy(() -> linkFacade.createLink(spaceId, memberId2, request))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    private void setUpTestData() {
        Member member1 = new Member(
                "123456",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "닉네임 데이터",
                "소개 데이터",
                "1111@gmail.com",
                false,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지1"),
                new FavoriteCategory(Category.ENTER_ART)
        );

        Member member2 = new Member(
                "1234563",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "닉네임 데이터2",
                "소개 데이터2",
                "2222@gmail.com",
                false,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지2"),
                new FavoriteCategory(Category.ENTER_ART)
        );


        memberId1 = memberJpaRepository.save(member1).getId();
        memberId2 = memberJpaRepository.save(member2).getId();

        //스페이스 생성 - member1
        Space space = new Space(
                memberId1,
                "스페이스의 제목",
                "스페이스 설명",
                Category.ENTER_ART,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(memberId1, Role.OWNER),
                true,
                true,
                true,
                true
        );

        //스페이스 참여 - member2
        space.addSpaceMember(
                new SpaceMember(memberId2, Role.CAN_VIEW)
        );

        spaceId = spaceJpaRepository.save(space).getId();
    }
}
