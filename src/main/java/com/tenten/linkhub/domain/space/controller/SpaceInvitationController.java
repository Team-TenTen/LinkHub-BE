package com.tenten.linkhub.domain.space.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.space.controller.dto.invitation.SpaceInvitationApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.invitation.SpaceInvitationApiResponse;
import com.tenten.linkhub.domain.space.controller.mapper.SpaceInvitationApiMapper;
import com.tenten.linkhub.domain.space.facade.SpaceInvitationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "spaces", description = "space 템플릿 API Document")
@RequiredArgsConstructor
@RestController
@RequestMapping("/spaces")
public class SpaceInvitationController {
    private static final String NOTIFICATION_LOCATION_PRE_FIX = "https://api.Link-hub.site/notifications";

    private final SpaceInvitationFacade spaceInvitationFacade;
    private final SpaceInvitationApiMapper mapper;

    /**
     * 스페이스 초대 API
     */
    @Operation(
            summary = "스페이스 초대 API", description = "스페이스 초대 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "스페이스 초대를 성공적으로 완료하였습니다.")
            })
    @PostMapping(
            value = "/invite",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceInvitationApiResponse> invite(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody @Valid SpaceInvitationApiRequest request
    ) {
        Long notificationId = spaceInvitationFacade.invite(mapper.toSpaceInvitationFacadeRequest(request, memberDetails.memberId()));

        SpaceInvitationApiResponse apiResponse = SpaceInvitationApiResponse.from(notificationId);

        return ResponseEntity
                .created(URI.create(NOTIFICATION_LOCATION_PRE_FIX))
                .body(apiResponse);
    }
}