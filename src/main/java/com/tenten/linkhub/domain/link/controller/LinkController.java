package com.tenten.linkhub.domain.link.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.link.controller.dto.LikeCreateApiResponse;
import com.tenten.linkhub.domain.link.controller.dto.LinkCreateApiRequest;
import com.tenten.linkhub.domain.link.controller.dto.LinkCreateApiResponse;
import com.tenten.linkhub.domain.link.controller.dto.LinkUpdateApiRequest;
import com.tenten.linkhub.domain.link.controller.dto.LinkUpdateApiResponse;
import com.tenten.linkhub.domain.link.controller.dto.LinksGetWithFilterApiRequest;
import com.tenten.linkhub.domain.link.controller.dto.LinksGetWithFilterApiResponses;
import com.tenten.linkhub.domain.link.controller.dto.PopularLinksGetApiResponses;
import com.tenten.linkhub.domain.link.controller.mapper.LinkApiMapper;
import com.tenten.linkhub.domain.link.facade.LinkFacade;
import com.tenten.linkhub.domain.link.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.link.facade.dto.LinkUpdateFacadeRequest;
import com.tenten.linkhub.domain.link.service.LinkService;
import com.tenten.linkhub.domain.link.service.dto.LinkGetByQueryResponses;
import com.tenten.linkhub.domain.link.service.dto.LinksGetByQueryRequest;
import com.tenten.linkhub.domain.link.service.dto.PopularLinksGetByQueryResponses;

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
    private final LinkService linkService;
    private final LinkApiMapper mapper;

    public LinkController(LinkFacade linkFacade,
                          LinkApiMapper mapper,
                          LinkService linkService) {
        this.linkFacade = linkFacade;
        this.mapper = mapper;
        this.linkService = linkService;
    }

    /**
     * 링크 생성 API
     */
    @Operation(
            summary = "링크 생성 API",
            description = "[JWT 필요] 스페이스 내에서 링크를 생성하는 API 입니다. TagName과 Color는 필수로 포함되어야 하는 값은 아닙니다. \n - TagName & Color를 포함하여 링크를 수정하는 경우: tagName & Color 필드 포함. 단, \"\" 나 \" \"를 허용하지 않습니다.\n - Tag를 포함하지 않고 링크를 생성하는 경우: 아예 tagName & Color 필드 없이 보내주세요",
            responses = {
                    @ApiResponse(responseCode = "201", description = "링크가 성공적으로 생성된 경우"),
                    @ApiResponse(responseCode = "404", description = "링크 생성 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 spaceId에 해당하는 스페이스를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
                    @ApiResponse(responseCode = "404", description = "링크 수정 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 spaceId에 해당하는 스페이스를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 linkId에 해당하는 스페이스를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
                    @ApiResponse(responseCode = "404", description = "이미 삭제하거나 존재하지 않는 좋아요인 경우",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
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
                    @ApiResponse(responseCode = "404", description = "링크 삭제 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 spaceId에 해당하는 스페이스를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "요청한 linkId에 해당하는 스페이스를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
    @Operation(
            summary = "링크 조회 API", description = "- tagId, pageNumber, pageSize, sort를 받아 검색합니다.(sort, filter 조건 없이 사용할 수 있습니다.) \n " +
            " - sort: {created_at, popular} -> sort를 넣어주지 않을 경우 default는 created_at입니다. \n " +
            " - tagId: 필터링하고자 하는 태그의 tagId를 넣어주세요. \n ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색이 성공적으로 완료 되었습니다."),
                    @ApiResponse(responseCode = "404", description = "링크를 조회할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
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

    /**
     * 인기 있는 링크 리스트 조회
     */
    @Operation(
            summary = "인기 있는 링크 조회 API", description = "랜딩 페이지에서 인기있는 리스트를 조회할 때 사용하는 API 입니다. \n  로그인이 되어있다면 JWT를 넣어 요청 보내주시면 됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인기 있는 링크 조회가 성공적으로 완료 되었습니다.")
            })
    @GetMapping("/links/popular")
    public ResponseEntity<PopularLinksGetApiResponses> getPopularLinks(
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        Long memberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();

        PopularLinksGetByQueryResponses responses = linkService.getPopularLinks(memberId);
        PopularLinksGetApiResponses apiResponses = PopularLinksGetApiResponses.from(responses);

        return ResponseEntity
                .ok()
                .body(apiResponses);
    }

}
