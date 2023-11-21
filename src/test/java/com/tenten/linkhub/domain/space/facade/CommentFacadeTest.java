package com.tenten.linkhub.domain.space.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.facade.dto.CommentAndChildCountAndMemberInfo;
import com.tenten.linkhub.domain.space.facade.dto.CommentAndChildCountAndMemberInfoResponses;
import com.tenten.linkhub.domain.space.facade.dto.RepliesAndMemberInfo;
import com.tenten.linkhub.domain.space.facade.dto.RepliesAndMemberInfoResponses;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.comment.CommentJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
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

    private Long setUpSpaceId1;
    private Long setUpSpaceId2;
    private Long setUpMemberId1;
    private Long setUpMemberId2;
    private Comment savedComment1;
    private Comment childComment1;
    private Comment childComment1OfChildComment1;
    private Comment childComment2OfChildComment1;

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @Test
    @DisplayName("유저는 수정 유무를 포함한 루트 댓글들을 페이징 조회한다.")
    void findRootComments() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        CommentAndChildCountAndMemberInfoResponses facadeResponses = commentFacade.findRootComments(setUpSpaceId1, setUpMemberId1, pageRequest);

        //then
        List<CommentAndChildCountAndMemberInfo> content = facadeResponses.responses().getContent();

        assertThat(content).hasSize(2);

        assertThat(content.get(0).content()).isEqualTo("첫번째 루트 댓글");
        assertThat(content.get(0).nickname()).isEqualTo("잠자는 사자의 콧털");
        assertThat(content.get(0).profileImagePath()).isEqualTo("https://testprofileimage");
        assertThat(content.get(0).childCount()).isEqualTo(2);
        assertThat(content.get(0).isModifiable()).isTrue();

        assertThat(content.get(1).content()).isEqualTo("두번째 루트 댓글");
        assertThat(content.get(1).nickname()).isEqualTo("테스트 유저");
        assertThat(content.get(1).childCount()).isEqualTo(1);
        assertThat(content.get(1).isModifiable()).isFalse();
    }

    @Test
    @DisplayName("댓글을 달 수 없는 스페이스의 루트 댓글 페이지 조회 시 UnauthorizedAccessException가 발생한다. ")
    void findRootComments_UnauthorizedAccessException() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when//then
        assertThatThrownBy(() -> commentFacade.findRootComments(setUpSpaceId2, setUpMemberId1, pageRequest))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("유저는 수정 여부를 포함한 대댓글들을 페이징 조회한다.")
    void findReplies() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        RepliesAndMemberInfoResponses facadeResponses = commentFacade.findReplies(
                setUpSpaceId1,
                savedComment1.getId(),
                setUpMemberId2,
                pageRequest);

        //then
        List<RepliesAndMemberInfo> content = facadeResponses.responses().getContent();

        assertThat(content).hasSize(4);

        assertThat(content.get(0).content()).isEqualTo("첫번째 루트 댓글의 대댓글1");
        assertThat(content.get(0).nickname()).isEqualTo("테스트 유저");
        assertThat(content.get(0).profileImagePath()).isEqualTo("https://testprofileimage1");
        assertThat(content.get(0).groupNumber()).isEqualTo(savedComment1.getId());
        assertThat(content.get(0).parentCommentId()).isEqualTo(savedComment1.getId());
        assertThat(content.get(0).isModifiable()).isTrue();

        assertThat(content.get(1).content()).isEqualTo("첫번째 루트 댓글의 대댓글1의 대댓글1");
        assertThat(content.get(1).nickname()).isEqualTo("테스트 유저");
        assertThat(content.get(1).profileImagePath()).isEqualTo("https://testprofileimage1");
        assertThat(content.get(1).groupNumber()).isEqualTo(savedComment1.getId());
        assertThat(content.get(1).parentCommentId()).isEqualTo(childComment1.getId());
        assertThat(content.get(1).isModifiable()).isTrue();

        assertThat(content.get(2).content()).isEqualTo("첫번째 루트 댓글의 대댓글2");
        assertThat(content.get(2).nickname()).isEqualTo("잠자는 사자의 콧털");
        assertThat(content.get(2).profileImagePath()).isEqualTo("https://testprofileimage");
        assertThat(content.get(2).groupNumber()).isEqualTo(savedComment1.getId());
        assertThat(content.get(2).parentCommentId()).isEqualTo(savedComment1.getId());
        assertThat(content.get(2).isModifiable()).isFalse();

        assertThat(content.get(3).content()).isEqualTo("첫번째 루트 댓글의 대댓글1의 대댓글2");
        assertThat(content.get(3).nickname()).isEqualTo("테스트 유저");
        assertThat(content.get(3).profileImagePath()).isEqualTo("https://testprofileimage1");
        assertThat(content.get(3).groupNumber()).isEqualTo(savedComment1.getId());
        assertThat(content.get(3).parentCommentId()).isEqualTo(childComment1.getId());
        assertThat(content.get(3).isModifiable()).isTrue();
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
        setUpMemberId1 = memberJpaRepository.save(member).getId();

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
        setUpMemberId2 = memberJpaRepository.save(member1).getId();

        Space space1 = new Space(
                setUpMemberId1,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(setUpMemberId1, Role.OWNER),
                true,
                true,
                true,
                true
        );

        Space space2 = new Space(
                setUpMemberId2,
                "두번째 스페이스",
                "두번째 스페이스 소개글",
                Category.LIFE_KNOWHOW_SHOPPING,
                new SpaceImage("https://testimage2", "테스트 이미지2"),
                new SpaceMember(setUpMemberId2, Role.OWNER),
                true,
                false,
                true,
                true
        );

        setUpSpaceId1 = spaceJpaRepository.save(space1).getId();
        setUpSpaceId2 = spaceJpaRepository.save(space2).getId();

        Comment comment1 = new Comment(
                null,
                null,
                "첫번째 루트 댓글",
                setUpMemberId1,
                space1
        );

        Comment comment2 = new Comment(
                null,
                null,
                "두번째 루트 댓글",
                setUpMemberId2,
                space1
        );

        savedComment1 = commentJpaRepository.save(comment1);
        Comment savedComment2 = commentJpaRepository.save(comment2);

        childComment1 = new Comment(
                savedComment1,
                savedComment1.getId(),
                "첫번째 루트 댓글의 대댓글1",
                setUpMemberId2,
                space1
        );

        childComment1OfChildComment1 = new Comment(
                childComment1,
                savedComment1.getId(),
                "첫번째 루트 댓글의 대댓글1의 대댓글1",
                setUpMemberId2,
                space1
        );

        Comment childComment2 = new Comment(
                savedComment1,
                savedComment1.getId(),
                "첫번째 루트 댓글의 대댓글2",
                setUpMemberId1,
                space1
        );

        Comment childComment3 = new Comment(
                savedComment2,
                savedComment2.getId(),
                "두번째 루트 댓글의 대댓글1",
                setUpMemberId1,
                space1
        );

        commentJpaRepository.save(childComment1);
        commentJpaRepository.save(childComment1OfChildComment1);
        commentJpaRepository.save(childComment2);
        commentJpaRepository.save(childComment3);

        childComment2OfChildComment1 = new Comment(
                childComment1,
                savedComment1.getId(),
                "첫번째 루트 댓글의 대댓글1의 대댓글2",
                setUpMemberId2,
                space1
        );

        commentJpaRepository.save(childComment2OfChildComment1);
    }

}
