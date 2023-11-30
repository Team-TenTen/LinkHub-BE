package com.tenten.linkhub.domain.notification.service;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.ProfileImage;
import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.member.repository.member.MemberJpaRepository;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInviteNotificationGetRequest;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInviteNotificationGetResponses;
import com.tenten.linkhub.domain.space.facade.SpaceInvitationFacade;
import com.tenten.linkhub.domain.space.facade.dto.SpaceInvitationFacadeRequest;
import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SpaceInvitationFacade spaceInvitationFacade;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Long invitingMemberId1;
    private Long invitingMemberId2;
    private Long invitedMemberId;
    private List<Long> spaceIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @Test
    @DisplayName("수신한 스페이스 초대 알림을 조회할 수 있다.")
    void getSpaceInvitations_memberId_Success() {
        //given - 2개의 스페이스에 각각 미리 invitedMember를 초대
        spaceInvitationFacade.invite(new SpaceInvitationFacadeRequest("invitedMember@gmail.com", spaceIds.get(0), Role.CAN_EDIT, invitingMemberId1));
        spaceInvitationFacade.invite(new SpaceInvitationFacadeRequest("invitedMember@gmail.com", spaceIds.get(1), Role.CAN_EDIT, invitingMemberId2));

        SpaceInviteNotificationGetRequest request = new SpaceInviteNotificationGetRequest(
                PageRequest.of(0, 10),
                invitedMemberId
        );

        //when
        SpaceInviteNotificationGetResponses spaceInvitations = notificationService.getSpaceInvitations(request);

        //then
        assertThat(spaceInvitations.responses().getContent().size()).isEqualTo(spaceIds.size());
        assertThat(spaceInvitations.responses().getContent().get(0).invitingMemberId()).isEqualTo(invitingMemberId1);
        assertThat(spaceInvitations.responses().getContent().get(1).invitingMemberId()).isEqualTo(invitingMemberId2);
    }

    @Test
    @DisplayName("사용자는 알림 내역을 삭제할 수 있다.")
    void deleteNotification_notificationIdAndMemberId_Success() {
        //given
        Long inviteNotificationId = spaceInvitationFacade.invite(new SpaceInvitationFacadeRequest("invitedMember@gmail.com", spaceIds.get(0), Role.CAN_EDIT, invitingMemberId1));

        //when
        notificationService.deleteNotification(inviteNotificationId, invitedMemberId);

        //then
        SpaceInviteNotificationGetRequest request = new SpaceInviteNotificationGetRequest(
                PageRequest.of(0, 10),
                invitedMemberId
        );
        SpaceInviteNotificationGetResponses spaceInvitations = notificationService.getSpaceInvitations(request);

        assertThat(spaceInvitations.responses().getContent().size()).isEqualTo(0);
    }

    private void setUpTestData() {
        Member invitingMember1 = new Member(
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

        Member invitingMember2 = new Member(
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

        Member invitedMember = new Member(
                "123456",
                Provider.kakao,
                com.tenten.linkhub.domain.member.model.Role.USER,
                "닉네임 데이터1",
                "소개 데이터1",
                "invitedMember@gmail.com",
                false,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지1"),
                new FavoriteCategory(Category.ENTER_ART)
        );

        invitingMemberId1 = memberJpaRepository.save(invitingMember1).getId();
        invitingMemberId2 = memberJpaRepository.save(invitingMember2).getId();
        invitedMemberId = memberJpaRepository.save(invitedMember).getId();

        spaceIds.clear();
        Space space1 = new Space(
                invitingMemberId1,
                "스페이스의 제목",
                "스페이스 설명",
                Category.ENTER_ART,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(invitingMemberId1, Role.OWNER),
                true,
                true,
                true,
                true
        );
        spaceJpaRepository.save(space1);
        spaceIds.add(space1.getId());

        Space space2 = new Space(
                invitingMemberId2,
                "스페이스의 제목",
                "스페이스 설명",
                Category.ENTER_ART,
                new SpaceImage("https://testimage1", "테스트 이미지1"),
                new SpaceMember(invitingMemberId2, Role.OWNER),
                true,
                true,
                true,
                true
        );
        spaceJpaRepository.save(space2);
        spaceIds.add(space2.getId());

    }
}
