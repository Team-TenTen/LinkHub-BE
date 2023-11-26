package com.tenten.linkhub.domain.notification.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.notification.controller.dto.SpaceInvitationGetApiRequest;
import com.tenten.linkhub.domain.notification.controller.dto.SpaceInvitationGetApiResponses;
import com.tenten.linkhub.domain.notification.controller.mapper.NotificationApiMapper;
import com.tenten.linkhub.domain.notification.service.NotificationService;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInvitationGetRequest;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInvitationGetResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notification", description = "notification 템플릿 API Document")
@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationApiMapper apiMapper;

    /**
     * 스페이스 초대 알림 조회 API
     */
    @GetMapping(value = "/invitations")
    public ResponseEntity<SpaceInvitationGetApiResponses> getInviteNotifications(
            @ModelAttribute SpaceInvitationGetApiRequest request,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        PageRequest pageRequest = PageRequest.of(
                request.pageNumber(),
                request.pageSize()
        );

        SpaceInvitationGetRequest serviceRequest = apiMapper.toSpaceInvitationGetRequest(
                pageRequest,
                memberDetails.memberId()
        );

        SpaceInvitationGetResponses responses = notificationService.getSpaceInvitations(serviceRequest);
        SpaceInvitationGetApiResponses apiResponses = SpaceInvitationGetApiResponses.from(responses);

        return ResponseEntity
                .ok()
                .body(apiResponses);
    }
}
