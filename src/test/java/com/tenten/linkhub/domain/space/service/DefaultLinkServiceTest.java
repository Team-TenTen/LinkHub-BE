package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.exception.LinkViewHistoryException;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.link.Color;
import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.model.link.vo.Url;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.link.LinkJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.link.LinkUpdateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DefaultLinkServiceTest {

    @Autowired
    private LinkService linkService;

    @Autowired
    private LinkJpaRepository linkJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    private Long memberId1;
    private Long spaceId;
    private Long linkId;

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @Test
    @DisplayName("사용자는 권한이 있는 경우 링크를 생성할 수 있다.")
    void createLink_request_Success() {
        //given
        LinkCreateRequest request = new LinkCreateRequest(
                spaceId,
                "https://mideveloperni.tistory.com/",
                "개발 블로그 1",
                "백엔드",
                memberId1,
                Color.BLUE
        );

        //when
        Long savedLinkId = linkService.createLink(request);

        //then
        Link savedLink = linkJpaRepository.findById(savedLinkId).get();

        assertThat(savedLink.getUrl()).usingRecursiveComparison().isEqualTo(new Url("https://mideveloperni.tistory.com/"));
        assertThat(savedLink.getTitle()).isEqualTo("개발 블로그 1");
        assertThat(savedLink.getTags().get(0).getName()).isEqualTo("백엔드");
    }

    @Test
    @DisplayName("사용자는 권한이 있는 경우 링크를 수정할 수 있다.")
    void updateLink_request_Success() {
        //given
        LinkUpdateRequest request = new LinkUpdateRequest(
                spaceId,
                "https://mideveloperni2.tistory.com/",
                "바꾼 타이틀",
                "바꾼 태그",
                memberId1,
                linkId,
                Color.GRAY
        );

        //when
        Long updatedLinkId = linkService.updateLink(request);

        //then
        Link savedLink = linkJpaRepository.findById(updatedLinkId).get();

        assertThat(savedLink.getUrl()).usingRecursiveComparison().isEqualTo(new Url("https://mideveloperni2.tistory.com/"));
        assertThat(savedLink.getTitle()).isEqualTo("바꾼 타이틀");
        assertThat(savedLink.getTags().get(0).getName()).isEqualTo("바꾼 태그");
    }

    @Test
    @DisplayName("이미 링크 접속 이력이 있을 경우 데이터를 저장할 수 없다.")
    void addLinkViewHistory_MemberIdAndSpaceIdAndLinkId_ThrowsException() {
        //given
        linkService.addLinkViewHistory(spaceId, linkId, memberId1);

        //when & then
        Assertions.assertThatThrownBy(() -> linkService.addLinkViewHistory(spaceId, linkId, memberId1))
                .isInstanceOf(LinkViewHistoryException.class);
    }

    @Test
    @DisplayName("사용자는 링크를 삭제할 수 있다.")
    void deleteLink_LinkId_Success() {
        //when
        linkService.deleteLink(linkId);

        //then
        int linkCount = (int) linkJpaRepository.findById(linkId).stream().count();
        assertThat(linkCount).isEqualTo(0);
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

        memberId1 = memberJpaRepository.save(member1).getId();

        //스페이스 생성 - member1가 owner
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

        spaceId = spaceJpaRepository.save(space).getId();

        //링크 생성
        Link link = Link.toLink(space, memberId1, "링크의 제목", new Url("https://www.naver.com"));
        linkId = linkJpaRepository.save(link).getId();
    }

}

