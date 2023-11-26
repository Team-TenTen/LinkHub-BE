package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.notification.model.Notification;
import com.tenten.linkhub.domain.notification.model.NotificationType;
import com.tenten.linkhub.domain.notification.repository.NotificationJpaRepository;
import com.tenten.linkhub.domain.space.facade.dto.SpaceInvitationFacadeRequest;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Invitation;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.invitation.InvitationJpaRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.invitation.SpaceInvitationAcceptRequest;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class SpaceInvitationFacadeTest {

    @Autowired
    private SpaceInvitationFacade spaceInvitationFacade;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private InvitationJpaRepository invitationJpaRepository;

    @Autowired
    private NotificationJpaRepository notificationJpaRepository;

    private Member myMember;
    private Member anotherMember;
    private Member invitedMember;

    private Space anotherSpace;
    private Notification myNotification;

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @Test
    @DisplayName("사용자는 다른 유저를 스페이스 멤버로 초대할 수 있다.")
    void invite_SpaceInvitationFacadeRequest_Success() {
        //given
        SpaceInvitationFacadeRequest request = new SpaceInvitationFacadeRequest(
                invitedMember.getId(),
                anotherSpace.getId(),
                Role.CAN_VIEW,
                anotherMember.getId()
        );

        //when
        Long notificationId = spaceInvitationFacade.invite(request);

        //then
        Notification notification = notificationJpaRepository.findById(notificationId).get();

        assertThat(notification.getRecipientId()).isEqualTo(invitedMember.getId());
        assertThat(notification.getSenderId()).isEqualTo(anotherMember.getId());
        assertThat(notification.getNotificationType()).isEqualTo(NotificationType.INVITATION);

        System.out.println(notification.getId());

        Invitation invitation = invitationJpaRepository.findByNotificationId(notificationId).get();

        assertThat(invitation.getNotificationId()).isEqualTo(notificationId);
        assertThat(invitation.getIsAccepted()).isFalse();
        assertThat(invitation.getMemberId()).isEqualTo(invitedMember.getId());
        assertThat(invitation.getSpace().getId()).isEqualTo(anotherSpace.getId());
        assertThat(invitation.getRole()).isEqualTo(Role.CAN_VIEW);
    }

    @Test
    @DisplayName("유저는 자신에게 온 스페이스 초대를 수락하여 해당 스페이스의 스페이스멤버가 될 수 있다.")
    void acceptSpaceInvitation() {
        //given
        SpaceInvitationAcceptRequest request = new SpaceInvitationAcceptRequest(myMember.getId(), myNotification.getId());

        //when
        Long spaceId = spaceInvitationFacade.acceptSpaceInvitation(request);

        //then
        Space space = spaceJpaRepository.findById(spaceId).get();

        assertThat(space.getSpaceName()).isEqualTo(anotherSpace.getSpaceName());
        assertThat(space.getSpaceMembers().get(1).getMemberId()).isEqualTo(myMember.getId());
        assertThat(space.getSpaceMembers().get(1).getRole()).isEqualTo(Role.CAN_EDIT);
    }

    @Test
    @DisplayName("다른 MemberId를 통해 스페이스 초대를 수락하면 UnauthorizedAccessException가 발생한다.")
    void acceptSpaceInvitation_UnauthorizedAccessException() {
        //given
        SpaceInvitationAcceptRequest request = new SpaceInvitationAcceptRequest(myMember.getId() + 10L, myNotification.getId());

        //when//then
        assertThatThrownBy(() -> spaceInvitationFacade.acceptSpaceInvitation(request))
                .isInstanceOf(UnauthorizedAccessException.class);
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
                "testSocialId",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "프롱이",
                "프롱이 소개글",
                "abc@gmail.com",
                true,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지"),
                new FavoriteCategory(Category.KNOWLEDGE_ISSUE_CAREER)
        );

        Member member3 = new Member(
                "testSocialId",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "풀스택",
                "풀스택 소개글",
                "abcd@gmail.com",
                true,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지"),
                new FavoriteCategory(Category.KNOWLEDGE_ISSUE_CAREER)
        );
        anotherMember = memberJpaRepository.save(member1);
        myMember = memberJpaRepository.save(member2);
        invitedMember = memberJpaRepository.save(member3);


        Space space = new Space(
                anotherMember.getId(),
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(anotherMember.getId(), Role.OWNER),
                false,
                true,
                true,
                true
        );
        anotherSpace = spaceJpaRepository.save(space);

        Notification notification = Notification
                .builder()
                .senderId(anotherMember.getId())
                .recipientId(myMember.getId())
                .notificationType(NotificationType.INVITATION)
                .build();
        myNotification = notificationJpaRepository.save(notification);

        Invitation invitation = new Invitation(
                space,
                Role.CAN_EDIT,
                myMember.getId(),
                myNotification.getId()
        );
        invitationJpaRepository.save(invitation);
    }

}