package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Favorite;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.favorite.FavoriteJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.favorite.SpaceRegisterInFavoriteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@TestPropertySource(locations = "classpath:/application-test.yml")
@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteJpaRepository favoriteJpaRepository;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Long setUpMemberId;
    private Long setUpSpaceId;

    @BeforeEach
    void setUp(){
        setUpTestData();
    }

    @Test
    @DisplayName("유저는 스페이스를 즐겨찾기에 등록할 수 있다.")
    void createFavorite() {
        //when
        SpaceRegisterInFavoriteResponse response = favoriteService.createFavorite(setUpSpaceId, setUpMemberId);

        //then
        Favorite savedFavorite = favoriteJpaRepository.findById(response.favoriteId()).get();

        assertThat(savedFavorite.getMemberId()).isEqualTo(setUpMemberId);
        assertThat(savedFavorite.getSpace().getSpaceName()).isEqualTo("첫번째 스페이스");
    }

    private void setUpTestData() {
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

        Space space = new Space(
                setUpMemberId + 10,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(setUpMemberId + 10, Role.OWNER),
                true,
                true,
                true,
                true
        );

        spaceJpaRepository.save(space);
        setUpSpaceId = spaceJpaRepository.save(space).getId();
    }
}