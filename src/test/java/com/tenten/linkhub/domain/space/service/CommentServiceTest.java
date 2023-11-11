package com.tenten.linkhub.domain.space.service;

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
import com.tenten.linkhub.domain.space.service.dto.comment.RootCommentCreateRequest;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@TestPropertySource(locations = "classpath:/application-test.yml")
@SpringBootTest
class CommentServiceTest {

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
    private Long setUpMemberId;

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @Test
    @DisplayName("유저는 스페이스에 댓글을 생성할 수 있다.")
    void createComment() {
        //given
        RootCommentCreateRequest reqeust = new RootCommentCreateRequest(
                setUpSpaceId1,
                setUpMemberId,
                "첫번째 댓글");

        //when
        Long savedCommentId = commentService.createComment(reqeust);

        //then
        Comment savedComment = commentJpaRepository.findById(savedCommentId).get();

        assertThat(savedComment.getContent()).isEqualTo("첫번째 댓글");
        assertThat(savedComment.getMemberId()).isEqualTo(setUpMemberId);
        assertThat(savedComment.getSpace().getSpaceName()).isEqualTo("첫번째 스페이스");
    }

    @Test
    @DisplayName("유저가 댓글 여부를 false로 설정한 스페이스에 댓글을 생성하려고 할 경우 예외가 발생한다.")
    void createComment_UnauthorizedAccessException() {
        //given
        RootCommentCreateRequest reqeust = new RootCommentCreateRequest(
                setUpSpaceId2,
                setUpMemberId,
                "첫번째 댓글");

        //when //then
        assertThatThrownBy(() -> commentService.createComment(reqeust))
                .isInstanceOf(UnauthorizedAccessException.class);

    }

    private void setUpTestData(){
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

        Space space1 = new Space(
                1L,
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

        Space space2 = new Space(
                1L,
                "두번째 스페이스",
                "두번째 스페이스 소개글",
                Category.ETC,
                new SpaceImage( "https://testimage2", "테스트 이미지2"),
                new SpaceMember(setUpMemberId, Role.OWNER),
                true,
                false,
                true,
                true
        );

        setUpSpaceId1 = spaceJpaRepository.save(space1).getId();
        setUpSpaceId2 = spaceJpaRepository.save(space2).getId();
    }

}
