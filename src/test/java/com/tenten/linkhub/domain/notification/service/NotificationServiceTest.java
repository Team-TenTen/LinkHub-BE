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

    private Long invitingMemberId;
    private Long invitedMemberId;
    private List<Long> spaceIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        setUpTestData();
    }

    @Test
    @DisplayName("수신한 스페이스 초대 알림을 조회할 수 있다.")
    void getSpaceInvitations_memberId_Success() {
        //given - 4개의 space에 미리 invitedMember를 초대
        for (Long spaceId : spaceIds) {
            spaceInvitationFacade.invite(new SpaceInvitationFacadeRequest("2222@gmail.com", spaceId, Role.CAN_EDIT, invitingMemberId));
        }

        SpaceInviteNotificationGetRequest request = new SpaceInviteNotificationGetRequest(
                PageRequest.of(0, 10),
                invitedMemberId
        );

        //when
        SpaceInviteNotificationGetResponses spaceInvitations = notificationService.getSpaceInvitations(request);

        //then
        assertThat(spaceInvitations.responses().getContent().size()).isEqualTo(spaceIds.size());
        for (int i = 0; i < spaceIds.size(); i++) {
            assertThat(spaceInvitations.responses().getContent().get(i).invitingMemberId()).isEqualTo(invitingMemberId);
        }
    }

    private void setUpTestData() {
        Member invitingMember = new Member(
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
                "2222@gmail.com",
                false,
                new ProfileImage("https://testprofileimage", "테스트용 멤버 프로필 이미지1"),
                new FavoriteCategory(Category.ENTER_ART)
        );

        invitingMemberId = memberJpaRepository.save(invitingMember).getId();
        invitedMemberId = memberJpaRepository.save(invitedMember).getId();

        spaceIds.clear();
        for (int i = 0; i < 4; i++) {
            Space space = new Space(
                    invitingMemberId,
                    "스페이스의 제목",
                    "스페이스 설명",
                    Category.ENTER_ART,
                    new SpaceImage("https://testimage1", "테스트 이미지1"),
                    new SpaceMember(invitingMemberId, Role.OWNER),
                    true,
                    true,
                    true,
                    true
            );
            spaceJpaRepository.save(space);
            spaceIds.add(space.getId());
        }
    }
}
