package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.IntegrationApplicationTest;
import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Comment;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.comment.CommentJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.ReplyCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.RootCommentCreateRequest;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CommentServiceTest extends IntegrationApplicationTest {

    @Autowired
    private CommentService commentService;

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
    private Long setUpCommentId1;
    private Long setUpCommentId2;

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @Test
    @DisplayName("유저는 스페이스에 댓글을 생성할 수 있다.")
    void createComment() {
        //given
        RootCommentCreateRequest request = new RootCommentCreateRequest(
                setUpSpaceId1,
                setUpMemberId1,
                "첫번째 댓글");

        //when
        Long savedCommentId = commentService.createComment(request);

        //then
        Comment savedComment = commentJpaRepository.findById(savedCommentId).get();

        assertThat(savedComment.getContent()).isEqualTo("첫번째 댓글");
        assertThat(savedComment.getMemberId()).isEqualTo(setUpMemberId1);
        assertThat(savedComment.getSpace().getSpaceName()).isEqualTo("첫번째 스페이스");
    }

    @Test
    @DisplayName("유저가 댓글 여부를 false로 설정한 스페이스에 댓글을 생성하려고 할 경우 예외가 발생한다.")
    void createComment_UnauthorizedAccessException() {
        //given
        RootCommentCreateRequest request = new RootCommentCreateRequest(
                setUpSpaceId2,
                setUpMemberId1,
                "첫번째 댓글");

        //when //then
        assertThatThrownBy(() -> commentService.createComment(request))
                .isInstanceOf(UnauthorizedAccessException.class);

    }

    @Test
    @DisplayName("유저가 자신이 작성한 댓글을 수정한다.")
    void updateComment() {
        //given
        CommentUpdateRequest request = new CommentUpdateRequest(
                setUpSpaceId1,
                setUpCommentId1,
                setUpMemberId1,
                "수정된 부모 댓글입니다.");

        //when
        Long updatedCommentId = commentService.updateComment(request);

        // then
        Comment updatedComment = commentJpaRepository.findById(updatedCommentId).get();

        assertThat(updatedComment.getId()).isEqualTo(updatedCommentId);
        assertThat(updatedComment.getContent()).isEqualTo("수정된 부모 댓글입니다.");
        assertThat(updatedComment.getMemberId()).isEqualTo(setUpMemberId1);
        assertThat(updatedComment.getSpace().getSpaceName()).isEqualTo("첫번째 스페이스");
    }

    @Test
    @DisplayName("유저가 존재하지 않는 댓글을 수정하는데 실패한다.")
    void updateComment_DataNotFoundException() {
        //given
        CommentUpdateRequest request = new CommentUpdateRequest(
                setUpSpaceId1,
                999L,
                setUpMemberId1,
                "수정된 부모 댓글입니다.");

        //when //then
        assertThatThrownBy(() -> commentService.updateComment(request))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @DisplayName("유저가 타인의 댓글을 수정하는데 실패한다.")
    void updateCommentOfOthers_UnauthorizedAccessException() {
        //given
        CommentUpdateRequest request = new CommentUpdateRequest(
                setUpSpaceId1,
                setUpCommentId1,
                setUpMemberId2,
                "수정된 부모 댓글입니다.");

        //when //then
        assertThatThrownBy(() -> commentService.updateComment(request))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("유저가 댓글을 달 수 없는 스페이스에서 댓글 수정을 실패한다.")
    void updateCommentFromNotAllowedSpace_UnauthorizedAccessException() {
        //given
        CommentUpdateRequest request = new CommentUpdateRequest(
                setUpSpaceId2,
                setUpCommentId2,
                setUpMemberId1,
                "수정된 부모 댓글입니다.");

        //when //then
        assertThatThrownBy(() -> commentService.updateComment(request))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("유저가 자신이 단 댓글을 삭제하는데 성공한다.")
    void deleteComment() {
        //given //when
        Long deletedCommentId = commentService.deleteComment(setUpSpaceId1, setUpCommentId1, setUpMemberId1);

        // then
        Optional<Comment> deletedComment = commentJpaRepository.findById(deletedCommentId);

        assertThat(deletedCommentId).isEqualTo(setUpCommentId1);
        assertThat(deletedComment).isEmpty();
    }

    @Test
    @DisplayName("유저가 존재하지 않는 댓글을 삭제하는데 실패한다.")
    void deleteComment_DataNotFoundException() {
        //given //when //then
        assertThatThrownBy(() -> commentService.deleteComment(setUpSpaceId1, 999L, setUpMemberId1))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @DisplayName("유저가 타인이 작성한 댓글을 삭제하는데 실패한다.")
    void deleteCommentOfOthers_UnauthorizedException() {
        //given //when //then
        assertThatThrownBy(() -> commentService.deleteComment(setUpSpaceId1, setUpCommentId1, setUpMemberId2))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("유저가 댓글 기능이 비활성화된 스페이스 내 댓글 삭제를 실패한다.")
    void deleteCommentFromNotAllowedSpace_UnauthorizedException() {
        //given //when //then
        assertThatThrownBy(() -> commentService.deleteComment(setUpSpaceId2, setUpCommentId2, setUpMemberId1))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("유저가 대댓글을 다는데 성공한다.")
    void createReply() {
        //given
        ReplyCreateRequest request = new ReplyCreateRequest(
                setUpSpaceId1,
                setUpCommentId1,
                setUpMemberId1,
                "댓글1에 대한 대댓글입니다.");

        //when
        Long createdReplyId = commentService.createReply(request);

        // then
        Comment createdReply = commentJpaRepository.findById(createdReplyId).get();

        assertThat(createdReply.getId()).isEqualTo(createdReplyId);
        assertThat(createdReply.getContent()).isEqualTo("댓글1에 대한 대댓글입니다.");
        assertThat(createdReply.getGroupNumber()).isEqualTo(setUpCommentId1);
        assertThat(createdReply.getSpace().getId()).isEqualTo(setUpSpaceId1);
    }

    @Test
    @DisplayName("유저가 댓글 기능이 비활성화된 스페이스에 대댓글 작성을 실패한다.")
    void createReply_UnauthorizedAccessException() {
        //given
        ReplyCreateRequest request = new ReplyCreateRequest(
                setUpSpaceId2,
                setUpCommentId2,
                setUpMemberId1,
                "댓글1에 대한 대댓글입니다.");

        //when //then
        assertThatThrownBy(() -> commentService.createReply(request))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    @DisplayName("유저가 존재하지 않는 부모 댓글에 대댓글 작성을 실패한다.")
    void createReply_DataNotFoundException() {
        //given
        ReplyCreateRequest request = new ReplyCreateRequest(
                setUpSpaceId1,
                999L,
                setUpMemberId1,
                "존재하지 않는 댓글999에 대한 대댓글입니다.");

        //when //then
        assertThatThrownBy(() -> commentService.createReply(request))
                .isInstanceOf(DataNotFoundException.class);
    }

    private void setUpTestData() {
        Member member1 = new Member(
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

        Member member2 = new Member(
                "testSocialId2",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "잠자는 사자의 포효",
                "테스트용 소개글",
                "abcd@gmail.com",
                true,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지"),
                new FavoriteCategory(Category.KNOWLEDGE_ISSUE_CAREER)
        );

        setUpMemberId1 = memberJpaRepository.save(member1).getId();
        setUpMemberId2 = memberJpaRepository.save(member2).getId();

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
                setUpMemberId1,
                "두번째 스페이스",
                "두번째 스페이스 소개글",
                Category.ETC,
                new SpaceImage("https://testimage2", "테스트 이미지2"),
                new SpaceMember(setUpMemberId1, Role.OWNER),
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
                "부모 댓글입니다.",
                member1.getId(),
                space1
        );

        Comment comment2 = new Comment(
                null,
                null,
                "부모 댓글입니다.",
                member1.getId(),
                space2
        );

        setUpCommentId1 = commentJpaRepository.save(comment1).getId();
        setUpCommentId2 = commentJpaRepository.save(comment2).getId();

    }

}
