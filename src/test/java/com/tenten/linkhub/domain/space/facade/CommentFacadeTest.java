package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.MemberJpaRepository;
import com.tenten.linkhub.domain.space.facade.dto.CommentAndChildCountAndMemberInfo;
import com.tenten.linkhub.domain.space.facade.dto.CommentAndChildCountAndMemberInfoResponses;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.comment.CommentJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@TestPropertySource(locations = "classpath:/application-test.yml")
@SpringBootTest
class CommentFacadeTest {

    @Autowired
    private CommentFacade commentFacade;

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Long setUpSpaceId;
    private Long setUpMemberId;

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @Test
    @DisplayName("유저는 루트 댓글들을 페이징 조회할 수 있다.")
    void findRootComments(){
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        CommentAndChildCountAndMemberInfoResponses facadeResponses = commentFacade.findRootComments(setUpSpaceId, pageRequest);

        //then
        List<CommentAndChildCountAndMemberInfo> content = facadeResponses.responses().getContent();

        assertThat(content.size()).isEqualTo(2);
        assertThat(content.get(0).content()).isEqualTo("첫번째 루트 댓글");
        assertThat(content.get(0).nickname()).isEqualTo("잠자는 사자의 콧털");
        assertThat(content.get(0).profileImagePath()).isEqualTo("https://testprofileimage");
        assertThat(content.get(0).childCount()).isEqualTo(2);
        assertThat(content.get(1).content()).isEqualTo("두번째 루트 댓글");
        assertThat(content.get(1).nickname()).isEqualTo("테스트 유저");
        assertThat(content.get(1).childCount()).isEqualTo(1);
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

        Member member1 = new Member(
                "testSocialId1",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "테스트 유저",
                "테스트용 소개글1",
                "abcde@gmail.com",
                true,
                new ProfileImage("https://testprofileimage1", "테스트용 멤버 프로필 이미지1"),
                new FavoriteCategory(Category.KNOWLEDGE_ISSUE_CAREER)
        );
        Long setUpMemberId1 = memberJpaRepository.save(member1).getId();

        Space space = new Space(
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
        setUpSpaceId = spaceJpaRepository.save(space).getId();

        Comment comment1 = new Comment(
                null,
                null,
                "첫번째 루트 댓글",
                setUpMemberId,
                space
        );

        Comment comment2 = new Comment(
                null,
                null,
                "두번째 루트 댓글",
                setUpMemberId1,
                space
        );

        Comment savedComment1 = commentJpaRepository.save(comment1);
        Comment savedComment2 = commentJpaRepository.save(comment2);

        Comment childComment1 = new Comment(
                savedComment1,
                savedComment1.getId(),
                "첫번째 루트 댓글의 대댓글1",
                setUpMemberId1,
                space
        );

        Comment childComment2 = new Comment(
                savedComment1,
                savedComment1.getId(),
                "첫번째 루트 댓글의 대댓글2",
                setUpMemberId,
                space
        );

        Comment childComment3 = new Comment(
                savedComment2,
                savedComment2.getId(),
                "두번째 루트 댓글의 대댓글1",
                setUpMemberId,
                space
        );

        commentJpaRepository.save(childComment1);
        commentJpaRepository.save(childComment2);
        commentJpaRepository.save(childComment3);

    }

}
