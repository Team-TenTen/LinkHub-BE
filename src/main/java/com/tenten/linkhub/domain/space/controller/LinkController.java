package com.tenten.linkhub.domain.space.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkUpdateApiResponse;
import com.tenten.linkhub.domain.space.controller.mapper.LinkApiMapper;
import com.tenten.linkhub.domain.space.facade.LinkFacade;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.LinkUpdateFacadeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LinkController {
    private static final String LINK_LOCATION_PREFIX = "https://api.Link-hub.site/links/";

    private final LinkFacade linkFacade;
    private final LinkApiMapper mapper;

    public LinkController(LinkFacade linkFacade, LinkApiMapper mapper) {
        this.linkFacade = linkFacade;
        this.mapper = mapper;
    }

    /**
     * 링크 생성 API
     */
    @Operation(
            summary = "링크 생성 API",
            description = "[JWT 필요] 스페이스 내에서 링크를 생성하는 API 입니다. Tag는 필수로 포함되어야 하는 값은 아닙니다. \n - Tag를 포함하여 링크를 생성하는 경우: tag 필드 포함. 단, \"\" 나 \" \"를 허용하지 않습니다.\n - Tag를 포함하지 않고 링크를 생성하는 경우: 아예 tag 필드 없이 보내주세요",
            responses = {
                    @ApiResponse(responseCode = "201", description = "링크가 성공적으로 생성된 경우"),
                    @ApiResponse(responseCode = "404", description = "링크 생성 권한이 없습니다."),
                    @ApiResponse(responseCode = "404", description = "요청한 spaceId에 해당하는 스페이스를 찾을 수 없습니다.")
            })
    @PostMapping(value = "/spaces/{spaceId}/links",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LinkCreateApiResponse> createLink(
            @PathVariable Long spaceId,
            @Valid @RequestBody LinkCreateApiRequest apiRequest,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        LinkCreateFacadeRequest request = mapper.toLinkCreateFacadeRequest(apiRequest);
        Long linkId = linkFacade.createLink(
                spaceId,
                memberDetails.memberId(),
                request);

        LinkCreateApiResponse response = mapper.toLinkCreateApiResponse(linkId);
        return ResponseEntity
                .created(URI.create(LINK_LOCATION_PREFIX + response.linkId()))
                .body(response);
    }

    /**
     * 링크 수정 API
     */
    @PutMapping(value = "/spaces/{spaceId}/links/{linkId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LinkUpdateApiResponse> updateLink(
            @PathVariable long spaceId,
            @PathVariable long linkId,
            @Valid @RequestBody LinkUpdateApiRequest apiRequest,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        LinkUpdateFacadeRequest request = mapper.toLinkUpdateFacadeRequest(apiRequest);
        long updateLinkId = linkFacade.updateLink(
                spaceId,
                linkId,
                memberDetails.memberId(),
                request
        );

        LinkUpdateApiResponse response = mapper.toLinkUpdateApiResponse(updateLinkId);
        return ResponseEntity
                .ok()
                .body(response);
    }
}
