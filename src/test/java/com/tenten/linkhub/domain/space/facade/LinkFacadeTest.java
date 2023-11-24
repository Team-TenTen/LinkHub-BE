package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.LinkUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.link.Like;
import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.model.link.vo.Url;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.like.LikeJpaRepository;
import com.tenten.linkhub.domain.space.repository.link.LinkJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.repository.spacemember.SpaceMemberJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.link.LinksGetByQueryRequest;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
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

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    private Long memberId1;
    private Long memberId2;
    private Long memberId3;
    private Long spaceId;
    private Long linkId;

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
                "태그의 이름",
                "red"
        );

        //when
        Long linkId = linkFacade.createLink(spaceId, memberId1, request);

        //then
        Link link = linkJpaRepository.findById(linkId).get();
        assertThat(link.getUrl().getUrl()).isEqualTo("https://naver.com");
        assertThat(link.getTitle()).isEqualTo("링크의 제목");
        assertThat(link.getLinkTags().get(0).getTag().getName()).isEqualTo("태그의 이름");
    }

    @Test
    @DisplayName("사용자는 CAN_EDIT이나 OWNER 권한이 아닌 경우 링크를 생성할 수 없다.")
    void createLink_request_ThrowsUnauthorizedAccessException() {
        //given
        LinkCreateFacadeRequest request = new LinkCreateFacadeRequest(
                "https://naver.com",
                "링크의 제목",
                "태그의 이름",
                "emerald"
        );

        //when
        Assertions.assertThatThrownBy(() -> linkFacade.createLink(spaceId, memberId2, request))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("사용자는 CAN_EDIT이나 OWNER 권한을 가진 경우 링크를 수정할 수 있다.")
    void updateLink_request_Success() {
        //given
        LinkUpdateFacadeRequest request = new LinkUpdateFacadeRequest(
                "https://naver2.com",
                "수정할 링크의 제목",
                "수정할 태그의 이름",
                "gray"
        );

        //when
        Long updateLinkId = linkFacade.updateLink(spaceId, linkId, memberId1, request);

        //then
        Link link = linkJpaRepository.findById(linkId).get();
        assertThat(link.getUrl().getUrl()).isEqualTo("https://naver2.com");
        assertThat(link.getTitle()).isEqualTo("수정할 링크의 제목");
        assertThat(link.getLinkTags().get(0).getTag().getName()).isEqualTo("수정할 태그의 이름");
    }

    @Test
    @DisplayName("사용자는 CAN_EDIT이나 OWNER 권한이 아닌 경우 링크를 수정할 수 없다.")
    void updateLink_request_ThrowsUnauthorizedAccessException() {
        //given
        LinkUpdateFacadeRequest request = new LinkUpdateFacadeRequest(
                "https://naver2.com",
                "수정할 링크의 제목",
                "수정할 태그의 이름",
                "blue"
        );

        //when
        Assertions.assertThatThrownBy(() -> linkFacade.updateLink(spaceId, linkId, memberId2, request))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("좋아요에 성공한다.")
    void createLike_LinkIdAndMemberId_Success() {
        //given & when
        linkFacade.createLike(linkId, memberId1);

        //then
        Optional<Like> like = likeJpaRepository.findByLinkIdAndMemberId(linkId, memberId1);
        assertThat(like).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 링크에 좋아요를 실패한다.")
    void createLike_LinkIdAndMemberId_ThrowsDataNotFoundException() {
        //given & when & then
        Assertions.assertThatThrownBy(() -> linkFacade.createLike(999L, memberId1))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @DisplayName("이미 좋아요한 링크에 좋아요를 실패한다.")
    void createLike_LinkIdAndMemberId_ThrowsUnauthorizedAccessException() {
        //given
        linkFacade.createLike(linkId, memberId1);

        //when & then
        Assertions.assertThatThrownBy(() -> linkFacade.createLike(linkId, memberId1))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("좋아요 한 링크의 좋아요를 취소한다.")
    void cancelLike_LinkIdAndMemberId_noContent() {
        //given
        linkFacade.createLike(linkId, memberId1);

        //when
        linkFacade.cancelLike(linkId, memberId1);

        //then
        Optional<Like> like = likeJpaRepository.findByLinkIdAndMemberId(linkId, memberId1);
        assertThat(like).isEmpty();
    }

    @Test
    @DisplayName("좋아요하지 않은 좋아요를 취소하는데 실패한다.")
    void cancelLike_LinkIdAndMemberId_DataNotFoundException() {
        //given //when //then
        Assertions.assertThatThrownBy(() -> linkFacade.cancelLike(linkId, memberId1))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @DisplayName("사용자는 CAN_EDIT이나 OWNER 권한이 아닌 경우 링크를 삭제할 수 없다.")
    void deleteLink_request_ThrowsUnauthorizedAccessException() {
        //when & then
        Assertions.assertThatThrownBy(() -> linkFacade.deleteLink(spaceId, linkId, memberId2))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("사용자는 스페이스 접근 권한이 없을 경우 링크를 조회할 수 없다.")
    void getLinks_request_ThrowsUnauthorizedAccessException() {
        //given
        LinksGetByQueryRequest request = new LinksGetByQueryRequest(
                PageRequest.of(0, 1),
                spaceId,
                memberId3,
                null
        );

        //when & then
        Assertions.assertThatThrownBy(() -> linkFacade.getLinks(request))
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

        Member member3 = new Member(
                "1234563",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "닉네임 데이터3",
                "소개 데이터3",
                "3333@gmail.com",
                false,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지3"),
                new FavoriteCategory(Category.ENTER_ART)
        );


        memberId1 = memberJpaRepository.save(member1).getId();
        memberId2 = memberJpaRepository.save(member2).getId();
        memberId3 = memberJpaRepository.save(member3).getId();

        //스페이스 생성 - member1
        Space space = new Space(
                memberId1,
                "스페이스의 제목",
                "스페이스 설명",
                Category.ENTER_ART,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(memberId1, Role.OWNER),
                false,
                true,
                true,
                true
        );

        //스페이스 참여 - member2
        space.addSpaceMember(
                new SpaceMember(memberId2, Role.CAN_VIEW)
        );

        spaceId = spaceJpaRepository.save(space).getId();

        //링크 생성 - link
        Link link = Link.toLink(
                space,
                memberId1,
                "보기 좋은 블로그 링크",
                new Url("https://www.tistory.com"));

        linkId = linkJpaRepository.save(link).getId();
    }


}
