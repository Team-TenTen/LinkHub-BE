package com.tenten.linkhub.domain.link.service;

import com.tenten.linkhub.IntegrationApplicationTest;
import com.tenten.linkhub.domain.link.service.LinkService;
import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.link.exception.LinkViewHistoryException;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.link.model.Link;
import com.tenten.linkhub.domain.link.model.vo.Url;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.link.repository.link.LinkJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.link.service.dto.LinkCreateRequest;
import com.tenten.linkhub.domain.link.service.dto.LinkGetByQueryResponses;
import com.tenten.linkhub.domain.link.service.dto.LinkUpdateRequest;
import com.tenten.linkhub.domain.link.service.dto.LinksGetByQueryRequest;
import com.tenten.linkhub.domain.link.service.dto.PopularLinksGetByQueryResponses;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class DefaultLinkServiceTest extends IntegrationApplicationTest {

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
    private List<Long> linkIds;

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
                "blue"
        );

        //when
        Long savedLinkId = linkService.createLink(request);

        //then
        Link savedLink = linkJpaRepository.findById(savedLinkId).get();

        assertThat(savedLink.getUrl()).usingRecursiveComparison().isEqualTo(new Url("https://mideveloperni.tistory.com/"));
        assertThat(savedLink.getTitle()).isEqualTo("개발 블로그 1");
        assertThat(savedLink.getLinkTags().get(0).getTag().getName()).isEqualTo("백엔드");
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
                "gray"
        );

        //when
        Long updatedLinkId = linkService.updateLink(request);

        //then
        Link savedLink = linkJpaRepository.findById(updatedLinkId).get();

        assertThat(savedLink.getUrl()).usingRecursiveComparison().isEqualTo(new Url("https://mideveloperni2.tistory.com/"));
        assertThat(savedLink.getTitle()).isEqualTo("바꾼 타이틀");
        assertThat(savedLink.getLinkTags().get(0).getTag().getName()).isEqualTo("바꾼 태그");
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

    @Test
    @DisplayName("사용자는 최신순으로 링크를 조회할 수 있다.")
    void getLinks_Request_SortByCreatedAtSuccess() {
        //given
        LinksGetByQueryRequest request = new LinksGetByQueryRequest(
                PageRequest.of(0, 10),
                spaceId,
                memberId1,
                null
        );

        //when
        LinkGetByQueryResponses links = linkService.getLinks(request);

        //then
        assertThat(links.responses().getContent().size()).isEqualTo(4);
        //최신 순 정렬 확인
        for (int i = 0; i < linkIds.size(); i++) {
            assertThat(linkIds.get(linkIds.size() - 1 - i)).isEqualTo(links.responses().getContent().get(i).linkId());
        }
    }

    @Test
    @DisplayName("사용자는 인기순으로 링크를 조회할 수 있다.")
    void getLinks_Request_SortByPopularSuccess() {
        //given
        LinksGetByQueryRequest request = new LinksGetByQueryRequest(
                PageRequest.of(0, 10, Sort.by("popular")),
                spaceId,
                memberId1,
                null
        );

        for (int i = 0; i < linkIds.size(); i++) {
            Link link = linkJpaRepository.findById(linkIds.get(i)).get();
            for (int j = 0; j < i; j++) {
                link.increaseLikeCount();
            }
        }

        //when
        LinkGetByQueryResponses links = linkService.getLinks(request);

        //then
        assertThat(links.responses().getContent().size()).isEqualTo(4);

        //인기 순 정렬 확인
        for (int i = 0; i < linkIds.size(); i++) {
            assertThat(linkIds.get(linkIds.size() - (i + 1))).isEqualTo(links.responses().getContent().get(i).linkId());
        }
    }

    @Test
    @DisplayName("사용자는 인기 있는 링크 리스트 5개를 확인할 수 있다.")
    void getPopularLinks_memberId_Success() {
        //given - 좋아요 수 증가
        for (int i = 0; i < linkIds.size(); i++) {
            Link link = linkJpaRepository.findById(linkIds.get(i)).get();
            for (int j = 0; j < i; j++) {
                link.increaseLikeCount();
            }
        }

        //when
        PopularLinksGetByQueryResponses popularLinks = linkService.getPopularLinks(memberId1);

        //then
        assertThat(popularLinks.responses()).hasSizeLessThanOrEqualTo(5);
        for (int i = 0; i < linkIds.size(); i++) {
            assertThat(linkIds.get(linkIds.size() - (i + 1))).isEqualTo(popularLinks.responses().get(i).linkId());
        }
    }

    @Test
    @DisplayName("로그인 한 사용자는 인기 있는 링크 리스트에서 본인이 좋아요를 누른 링크인지 여부를 확인할 수 있다.")
    void getPopularLinks2_memberId_Success() {
        //given
        linkService.createLike(linkIds.get(0), memberId1);

        //when
        PopularLinksGetByQueryResponses popularLinks = linkService.getPopularLinks(memberId1);

        //then
        assertThat(popularLinks.responses()).hasSizeLessThanOrEqualTo(5);
        assertThat(popularLinks.responses().get(0).isLiked()).isTrue();
        for (int i = 1; i < linkIds.size(); i++) {
            assertThat(popularLinks.responses().get(i).isLiked()).isFalse();
        }

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
        Link link0 = Link.toLink(space, memberId1, "링크의 제목", new Url("https://www.naver.com"));
        Link link1 = Link.toLink(space, memberId1, "링크의 제목1", new Url("https://www.naver.com"));
        Link link2 = Link.toLink(space, memberId1, "링크의 제목2", new Url("https://www.naver.com"));
        Link link3 = Link.toLink(space, memberId1, "링크의 제목3", new Url("https://www.naver.com"));


        linkId = linkJpaRepository.save(link0).getId();
        linkIds = List.of(
                linkId,
                linkJpaRepository.save(link1).getId(),
                linkJpaRepository.save(link2).getId(),
                linkJpaRepository.save(link3).getId()
        );


    }

}

