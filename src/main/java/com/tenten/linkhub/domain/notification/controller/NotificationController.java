package com.tenten.linkhub.domain.notification.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.notification.controller.dto.SpaceInviteNotificationGetApiRequest;
import com.tenten.linkhub.domain.notification.controller.dto.SpaceInviteNotificationGetApiResponses;
import com.tenten.linkhub.domain.notification.controller.mapper.NotificationApiMapper;
import com.tenten.linkhub.domain.notification.service.NotificationService;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInviteNotificationGetRequest;
import com.tenten.linkhub.domain.notification.service.dto.SpaceInviteNotificationGetResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Operation(
            summary = "스페이스 초대 알림 조회 API ", description = "[JWT 필요] - pageNumber, pageSize를 받아 스페이스 초대 알림 내역을 조회합니다. ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "초대 알림 조회가 성공적으로 완료되었습니다.")
            })
    @GetMapping(value = "/invitations")
    public ResponseEntity<SpaceInviteNotificationGetApiResponses> getInviteNotifications(
            @ModelAttribute SpaceInviteNotificationGetApiRequest request,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        PageRequest pageRequest = PageRequest.of(
                request.pageNumber(),
                request.pageSize()
        );

        SpaceInviteNotificationGetRequest serviceRequest = apiMapper.toSpaceInvitationGetRequest(
                pageRequest,
                memberDetails.memberId()
        );

        SpaceInviteNotificationGetResponses responses = notificationService.getSpaceInvitations(serviceRequest);
        SpaceInviteNotificationGetApiResponses apiResponses = SpaceInviteNotificationGetApiResponses.from(responses);

        return ResponseEntity
                .ok()
                .body(apiResponses);
    }

    /**
     * 알림 삭제 API
     */
    @Operation(
            summary = "알림 삭제 API ", description = "[JWT 필요] - notificationId를 받아 알림을 삭제해주는 API 입니다. 초대 알림의 경우 초대 내역도 함께 삭제합니다. ",
            responses = {
                    @ApiResponse(responseCode = "204", description = "알림 삭제 요청이 성공적으로 완료되었습니다.")
            })
    @DeleteMapping(value = "/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        notificationService.deleteNotification(notificationId, memberDetails.memberId());

        return ResponseEntity
                .noContent()
                .build();
    }
}
