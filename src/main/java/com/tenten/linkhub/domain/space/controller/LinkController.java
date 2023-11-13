package com.tenten.linkhub.domain.space.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.space.controller.dto.like.LikeCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.mapper.LinkApiMapper;
import com.tenten.linkhub.domain.space.facade.LinkFacade;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.global.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
     * 링크 좋아요 API
     */
    @Operation(
            summary = "링크 좋아요 API",
            description = "[JWT 필요] 링크에 좋아요를 누르는 기능입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "좋아요가 성공한 경우"),
                    @ApiResponse(responseCode = "404", description = "이미 좋아요한 링크인 경우 또는 존재하지 않는 링크인 경우",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    @PostMapping(value = "/links/{linkId}/like", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LikeCreateApiResponse> createLike(
            @PathVariable Long linkId,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        Boolean isLiked = linkFacade.createLike(linkId, memberDetails.memberId());

        return ResponseEntity.ok().body(LikeCreateApiResponse.from(isLiked));
    }

    /**
     * 링크 좋아요 취소 API
     */
    @Operation(
            summary = "링크 좋아요 취소 API",
            description = "[JWT 필요] 링크에 누른 좋아요를 취소하는 기능입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "좋아요 취소를 성공한 경우"),
            })
    @DeleteMapping(value = "/links/{linkId}/like", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> cancelLike(
            @PathVariable Long linkId,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        linkFacade.cancelLike(linkId, memberDetails.memberId());

        return ResponseEntity.noContent().build();
    }
}
