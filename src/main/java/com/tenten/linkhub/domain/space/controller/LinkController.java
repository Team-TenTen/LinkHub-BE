package com.tenten.linkhub.domain.space.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.space.controller.dto.like.LikeCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.link.LinksGetWithFilterApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinksGetWithFilterApiResponses;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkUpdateApiResponse;
import com.tenten.linkhub.domain.space.controller.mapper.LinkApiMapper;
import com.tenten.linkhub.domain.space.facade.LinkFacade;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.LinkUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.service.dto.link.LinkGetByQueryResponses;
import com.tenten.linkhub.domain.space.service.dto.link.LinksGetByQueryRequest;
import com.tenten.linkhub.global.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

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
            description = "[JWT 필요] 스페이스 내에서 링크를 생성하는 API 입니다. TagName과 Color는 필수로 포함되어야 하는 값은 아닙니다. \n - TagName & Color를 포함하여 링크를 수정하는 경우: tagName & Color 필드 포함. 단, \"\" 나 \" \"를 허용하지 않습니다.\n - Tag를 포함하지 않고 링크를 생성하는 경우: 아예 tagName & Color 필드 없이 보내주세요",
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
    @Operation(
            summary = "링크 수정 API",
            description = "[JWT 필요] 스페이스 내에서 링크를 수정하는 API 입니다. TagName과 Color는 필수로 포함되어야 하는 값은 아닙니다. \n - TagName & Color를 포함하여 링크를 수정하는 경우: tagName & Color 필드 포함. 단, \"\" 나 \" \"를 허용하지 않습니다.\n - Tag를 포함하지 않고 링크를 수정하는 경우: 아예 tagName & Color 필드 없이 보내주세요",
            responses = {
                    @ApiResponse(responseCode = "200", description = "링크가 성공적으로 수정된 경우"),
                    @ApiResponse(responseCode = "404", description = "링크 수정 권한이 없습니다."),
                    @ApiResponse(responseCode = "404", description = "요청한 spaceId에 해당하는 스페이스를 찾을 수 없습니다."),
                    @ApiResponse(responseCode = "404", description = "요청한 linkId에 해당하는 스페이스를 찾을 수 없습니다.")
            })
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

    /**
     * 링크에 접속시 접속 정보를 저장하는 API
     */
    @Operation(
            summary = "링크 접속 정보 저장 API",
            description = "[JWT 필요] 링크 접속 시 접속 정보를 저장하는 API입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "링크 접속 이력 요청을 성공적으로 처리했습니다."),
                    @ApiResponse(responseCode = "404", description = "스페이스 정보를 찾을 수 없습니다."),
                    @ApiResponse(responseCode = "404", description = "링크 정보를 찾을 수 없습니다.")
            })
    @PostMapping(value = "/spaces/{spaceId}/links/{linkId}/view")
    public ResponseEntity<Void> addLinkViewHistory(
            @PathVariable Long spaceId,
            @PathVariable Long linkId,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        linkFacade.addLinkViewHistory(spaceId, linkId, memberDetails.memberId());

        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * 링크 삭제 API
     */
    @Operation(
            summary = "링크 삭제 API",
            description = "[JWT 필요] 스페이스 내에서 링크를 삭제하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "링크가 성공적으로 삭제된 경우"),
                    @ApiResponse(responseCode = "404", description = "링크 삭제 권한이 없습니다."),
                    @ApiResponse(responseCode = "404", description = "요청한 spaceId에 해당하는 스페이스를 찾을 수 없습니다."),
                    @ApiResponse(responseCode = "404", description = "요청한 linkId에 해당하는 스페이스를 찾을 수 없습니다.")
            })
    @DeleteMapping(value = "/spaces/{spaceId}/links/{linkId}")
    public ResponseEntity<Void> deleteLink(
            @PathVariable Long spaceId,
            @PathVariable Long linkId,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        linkFacade.deleteLink(spaceId, linkId, memberDetails.memberId());

        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * 링크 조회 API
     */
    @GetMapping(value = "/spaces/{spaceId}/links")
    public ResponseEntity<LinksGetWithFilterApiResponses> getLinks(
            @PathVariable Long spaceId,
            @ModelAttribute LinksGetWithFilterApiRequest request,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        Long memberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();

        PageRequest pageRequest = PageRequest.of(
                request.pageNumber(),
                request.pageSize(),
                StringUtils.hasText(request.sort()) ? Sort.by(request.sort()) : Sort.unsorted());

        LinksGetByQueryRequest serviceRequest = mapper.toLinksGetByQueryRequest(
                request,
                pageRequest,
                spaceId,
                memberId
        );

        LinkGetByQueryResponses facadeResponses = linkFacade.getLinks(serviceRequest);
        LinksGetWithFilterApiResponses response = LinksGetWithFilterApiResponses.from(facadeResponses);

        return ResponseEntity
                .ok()
                .body(response);
    }

}
