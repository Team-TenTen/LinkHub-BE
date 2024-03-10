package com.tenten.linkhub.domain.space.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;

import com.tenten.linkhub.domain.space.common.SpaceCursorPageRequest;
import com.tenten.linkhub.domain.space.controller.dto.comment.CommentUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.comment.CommentUpdateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.comment.RepliesFindApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.comment.RepliesFindApiResponses;
import com.tenten.linkhub.domain.space.controller.dto.comment.ReplyCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.comment.ReplyCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.comment.RootCommentCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.comment.RootCommentCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.comment.RootCommentFindApiResponses;
import com.tenten.linkhub.domain.space.controller.dto.comment.RootCommentsFindApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.favorite.MyFavoriteSpacesFindApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.favorite.MyFavoriteSpacesFindApiResponses;
import com.tenten.linkhub.domain.space.controller.dto.favorite.SpaceRegisterInFavoriteApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.NewSpacesScrapApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.NewSpacesScrapApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.PublicSpaceFindWithFilterApiResponses;
import com.tenten.linkhub.domain.space.controller.dto.space.PublicSpacesFindByQueryApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.PublicSpacesFindByQueryApiResponses;
import com.tenten.linkhub.domain.space.controller.dto.space.PublicSpacesFindWithFilterApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceDetailGetByIdApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceTagsGetApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceUpdateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.spacemember.SpaceMemberRoleChangeApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.spacemember.SpaceMemberRoleChangeApiResponse;
import com.tenten.linkhub.domain.space.controller.mapper.CommentApiMapper;
import com.tenten.linkhub.domain.space.controller.mapper.FavoriteApiMapper;
import com.tenten.linkhub.domain.space.controller.mapper.SpaceApiMapper;
import com.tenten.linkhub.domain.space.controller.annotation.SpaceViewList;
import com.tenten.linkhub.domain.space.facade.CommentFacade;
import com.tenten.linkhub.domain.space.facade.SpaceFacade;
import com.tenten.linkhub.domain.space.facade.dto.CommentAndChildCountAndMemberInfoResponses;
import com.tenten.linkhub.domain.space.facade.dto.NewSpacesScrapFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.RepliesAndMemberInfoResponses;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.service.CommentService;
import com.tenten.linkhub.domain.space.service.FavoriteService;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.ReplyCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.comment.RootCommentCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.favorite.FavoriteSpacesFindResponses;
import com.tenten.linkhub.domain.space.service.dto.favorite.SpaceRegisterInFavoriteResponse;
import com.tenten.linkhub.domain.space.service.dto.space.PublicSpacesFindWithFilterRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.dto.space.SpaceTagGetResponses;

import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindWithCursorResponses;
import com.tenten.linkhub.global.response.ErrorResponse;
import com.tenten.linkhub.global.response.ErrorWithDetailCodeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "spaces", description = "space 템플릿 API Document")
@RequiredArgsConstructor
@RestController
@RequestMapping("/spaces")
public class SpaceController {
    private static final String SPACE_LOCATION_PRE_FIX = "https://api.Link-hub.site/spaces/";
    private static final int COOKIE_EXPIRE_TIME = 60 * 60 * 24;

    private final SpaceFacade spaceFacade;
    private final SpaceService spaceService;
    private final CommentFacade commentFacade;
    private final CommentService commentService;
    private final FavoriteService favoriteService;

    private final SpaceApiMapper spaceMapper;
    private final CommentApiMapper commentMapper;
    private final FavoriteApiMapper favoriteMapper;

    /**
     * 스페이스 검색 API
     */
    @Operation(
            summary = "스페이스 검색 API", description = "keyWord, pageNumber, pageSize, sort, filter를 받아 검색합니다.(keyWord, sort, filter 조건 없이 사용 가능합니다.)\n\n" +
            "sort: {created_at, updated_at, favorite_count, view_count}\n\n" +
            "filter: {ENTER_ART, LIFE_KNOWHOW_SHOPPING, HOBBY_LEISURE_TRAVEL, KNOWLEDGE_ISSUE_CAREER, ETC}",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색이 성공적으로 완료 되었습니다."),
            })
    @GetMapping(value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicSpacesFindByQueryApiResponses> findPublicSpacesByQuery(
            @ModelAttribute PublicSpacesFindByQueryApiRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(
                request.pageNumber(),
                request.pageSize(),
                StringUtils.hasText(request.sort()) ? Sort.by(request.sort()) : Sort.unsorted());

        SpacesFindByQueryResponses responses = spaceService.searchPublicSpacesByQuery(
                spaceMapper.toPublicSpacesFindByQueryRequest(request, pageRequest)
        );

        PublicSpacesFindByQueryApiResponses apiResponses = PublicSpacesFindByQueryApiResponses.from(responses);
        return ResponseEntity.ok(apiResponses);
    }

    /**
     * 스페이스 검색(like 쿼리) API
     */
    @Operation(
            summary = "스페이스 검색 API(like 쿼리)", description = "keyWord, pageNumber, pageSize, sort, filter를 받아 검색합니다.(keyWord, sort, filter 조건 없이 사용 가능합니다.)\n\n" +
            "sort: {created_at, updated_at, favorite_count, view_count}\n\n" +
            "filter: {ENTER_ART, LIFE_KNOWHOW_SHOPPING, HOBBY_LEISURE_TRAVEL, KNOWLEDGE_ISSUE_CAREER, ETC}",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색이 성공적으로 완료 되었습니다."),
            })
    @GetMapping(value = "/search/like-query",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicSpacesFindByQueryApiResponses> findPublicSpacesByLikeQuery(
            @ModelAttribute PublicSpacesFindByQueryApiRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(
                request.pageNumber(),
                request.pageSize(),
                StringUtils.hasText(request.sort()) ? Sort.by(request.sort()) : Sort.unsorted());

        SpacesFindByQueryResponses responses = spaceService.findPublicSpacesByLikeQuery(
                spaceMapper.toPublicSpacesFindByQueryRequest(request, pageRequest)
        );

        PublicSpacesFindByQueryApiResponses apiResponses = PublicSpacesFindByQueryApiResponses.from(responses);
        return ResponseEntity.ok(apiResponses);
    }

    /**
     * 스페이스 생성 API
     */
    @Operation(
            summary = "스페이스 생성 API", description = "스페이스 생성 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "스페이스가 성공적으로 생성되었습니다."),
                    @ApiResponse(responseCode = "400", description = "올바르지 않은 요청이 들어왔습니다.(G004: 요청 파라미터가 올바르지 않음./ G003: 지원하지 않는 contentType이 요청됨.)",
                            content = @Content(schema = @Schema(implementation = ErrorWithDetailCodeResponse.class)))
            })
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceCreateApiResponse> createSpace(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @Parameter(
                    description = "이미지 파일 외의 데이터는 application/json 타입으로 받습니다.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestPart @Valid SpaceCreateApiRequest request,
            @RequestPart(required = false) MultipartFile file
    ) {
        Long savedSpaceId = spaceFacade.createSpace(spaceMapper.toSpaceCreateFacadeRequest(request, file, memberDetails.memberId()));

        SpaceCreateApiResponse apiResponse = SpaceCreateApiResponse.from(savedSpaceId);

        return ResponseEntity
                .created(URI.create(SPACE_LOCATION_PRE_FIX + savedSpaceId))
                .body(apiResponse);
    }

    /**
     * 스페이스 상세 조회 API
     */
    @Operation(
            summary = "스페이스 상세 조회 API", description = "스페이스 상세 조회 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스페이스가 성공적으로 조회되었습니다."),
                    @ApiResponse(responseCode = "404", description = "요청한 spaceId에 해당하는 스페이스를 찾을 수 없습니다./ 권한으로 인해 스페이스를 조회할 수 없음.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping(value = "/{spaceId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceDetailGetByIdApiResponse> getSpaceDetailById(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId,
            @Parameter(hidden = true)
            @SpaceViewList List<Long> spaceViews,
            HttpServletResponse servletResponse
    ) {
        Long memberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();
        SpaceDetailGetByIdFacadeRequest request = spaceMapper.toSpaceDetailGetByIdFacadeRequest(spaceId, memberId, spaceViews);

        SpaceDetailGetByIdFacadeResponse response = spaceFacade.getSpaceDetailById(request);
        SpaceDetailGetByIdApiResponse apiResponse = SpaceDetailGetByIdApiResponse.from(response);

        setSpaceViewCookie(servletResponse, response.spaceViews());

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 스페이스 수정 API
     */
    @Operation(
            summary = "스페이스 정보 수정 API", description = "스페이스 정보 수정 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스페이스가 성공적으로 수정되었습니다."),
                    @ApiResponse(responseCode = "404", description = "스페이스를 찾을 수 없습니다./ 권한이 없는 유저가 스페이스를 수정하려고 합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "(G004: 필수 파라미터 누락 및 서버에서 지원하지 않는 타입 및 제한 보다 큰 사이즈의 파라미터가 요청되었습니다.)",
                            content = @Content(schema = @Schema(implementation = ErrorWithDetailCodeResponse.class)))
            })
    @PatchMapping(value = "/{spaceId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceUpdateApiResponse> updateSpace(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId,
            @Parameter(
                    description = "이미지 파일과 spaceId 외의 데이터는 application/json 타입으로 받습니다."
            )
            @RequestPart @Valid SpaceUpdateApiRequest request,
            @RequestPart(required = false) MultipartFile file
    ) {
        Long updatedSpaceId = spaceFacade.updateSpace(
                spaceMapper.toSpaceUpdateFacadeRequest(spaceId, request, file, memberDetails.memberId()));

        SpaceUpdateApiResponse apiResponse = SpaceUpdateApiResponse.from(updatedSpaceId);

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 스페이스 멤버 권한 수정 API
     */
    @Operation(
            summary = "스페이스 멤버 권한 수정 API", description = "스페이스 멤버 권한 수정 API 입니다.\n\n" +
            "role: {OWNER, CAN_EDIT, CAN_VIEW}",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스페이스 멤버의 권한 수정이 성공적으로 수정되었습니다."),
                    @ApiResponse(responseCode = "404", description = "권한이 없는 유저가 스페이스 멤버의 권한을 수정하려고 합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PatchMapping(value = "/{spaceId}/members/role",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceMemberRoleChangeApiResponse> changeSpaceMemberRole(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId,
            @RequestBody SpaceMemberRoleChangeApiRequest request
    ) {
        Long responseSpaceId = spaceService.changeSpaceMembersRole(
                spaceMapper.toSpaceMemberRoleChangeRequest(spaceId, memberDetails.memberId(), request)
        );

        SpaceMemberRoleChangeApiResponse apiResponse = SpaceMemberRoleChangeApiResponse.from(responseSpaceId);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 스페이스 삭제 API
     */
    @Operation(
            summary = "스페이스 삭제 API", description = "스페이스 삭제 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "스페이스가 성공적으로 삭제되었습니다."),
                    @ApiResponse(responseCode = "404", description = "권한이 없는 유저가 스페이스를 삭제하려고 합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{spaceId}")
    public ResponseEntity<Void> deleteSpace(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId
    ) {
        spaceFacade.deleteSpace(spaceId, memberDetails.memberId());

        return ResponseEntity.noContent().build();
    }

    /**
     * 스페이스 필터 조회 API
     */
    @Operation(
            summary = "스페이스 필터 조회 API", description = "메인 페이지용 스페이스 필터 조회이며 lastSpaceId(정렬 조건 favorite_count인 경우 lastFavoriteCount 추가로 필요), pageSize, sort, filter를 받아 조회합니다. (sort, filter조건 없이 사용 가능합니다.)\n\n " +
            "첫 페이지 조회의 경우 lastSpaceId 없이 요청하면 됩니다. (정렬 조건 favorite_count의 경우 lastSpaceId, lastFavoriteCount 둘다 없이 요청.)\n\n " +
            "sort: {created_at, favorite_count}\n\n " +
            "filter: {ENTER_ART, LIFE_KNOWHOW_SHOPPING, HOBBY_LEISURE_TRAVEL, KNOWLEDGE_ISSUE_CAREER, ETC}",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회가 성공적으로 완료 되었습니다."),
            })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicSpaceFindWithFilterApiResponses> findPublicSpacesWithFilter(
            @ModelAttribute PublicSpacesFindWithFilterApiRequest request
    ) {
        SpaceCursorPageRequest pageRequest = SpaceCursorPageRequest.of(
                request.pageSize(),
                request.sort(),
                request.filter());

        PublicSpacesFindWithFilterRequest serviceRequest = spaceMapper.toPublicSpacesFindWithFilterRequest(request, pageRequest);
        SpacesFindWithCursorResponses responses = spaceService.findPublicSpacesWithFilter(serviceRequest);

        PublicSpaceFindWithFilterApiResponses apiResponses = PublicSpaceFindWithFilterApiResponses.from(responses);

        return ResponseEntity.ok(apiResponses);
    }

    /**
     * 루트 댓글 생성 API
     */
    @Operation(
            summary = "루트 댓글 생성 API", description = "루트 댓글 생성 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "루트 댓글이 성공적으로 생성되었습니다."),
                    @ApiResponse(responseCode = "404", description = "댓글을 달 수 없는 스페이스에 댓글을 생성하려고 합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "G004: 필수 파라미터 누락 및 서버에서 지원하지 않는 타입 및 제한 보다 큰 사이즈의 파라미터가 요청되었습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorWithDetailCodeResponse.class)))
            })
    @PostMapping(value = "/{spaceId}/comments",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RootCommentCreateApiResponse> createRootComment(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId,
            @RequestBody @Valid RootCommentCreateApiRequest request
    ) {
        RootCommentCreateRequest apiRequest = commentMapper.toRootCommentCreateRequest(spaceId, memberDetails.memberId(), request.content());
        Long savedCommentId = commentService.createComment(apiRequest);

        RootCommentCreateApiResponse apiResponse = RootCommentCreateApiResponse.from(savedCommentId);

        return ResponseEntity
                .created(URI.create(SPACE_LOCATION_PRE_FIX + "/commments/" + savedCommentId))
                .body(apiResponse);
    }

    /**
     * 대댓글 생성 API
     */
    @Operation(
            summary = "대댓글 생성 API", description = "대댓글 댓글 생성 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "대댓글이 성공적으로 생성되었습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 스페이스 / 대댓글을 달 수 없는 스페이스 / 존재하지 않는 부모 댓글",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "G004: 필수 파라미터 누락 및 서버에서 지원하지 않는 타입 및 제한 보다 큰 사이즈의 파라미터가 요청되었습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorWithDetailCodeResponse.class)))
            })
    @PostMapping(value = "/{spaceId}/comments/{commentId}/replies",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReplyCreateApiResponse> createReply(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId,
            @PathVariable Long commentId,
            @RequestBody @Valid ReplyCreateApiRequest request
    ) {
        ReplyCreateRequest apiRequest = commentMapper.toReplyCreateRequest(spaceId, commentId, memberDetails.memberId(), request.content());
        Long savedCommentId = commentService.createReply(apiRequest);

        ReplyCreateApiResponse apiResponse = ReplyCreateApiResponse.from(savedCommentId);

        return ResponseEntity
                .created(URI.create(SPACE_LOCATION_PRE_FIX + "/commments/" + savedCommentId))
                .body(apiResponse);
    }

    /**
     * 스페이스에서 사용된 태그 목록 조회 API
     */
    @Operation(
            summary = "스페이스 내 태그 목록 조회 API", description = "스페이스 내 생성된 태그 목록을 조회하는 API 입니다. ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스페이스 내 생성된 태그 목록을 정상적으로 조회했습니다.")
            })
    @GetMapping(value = "/{spaceId}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceTagsGetApiResponse> getSpaceTags(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId
    ) {
        SpaceTagGetResponses response = spaceService.getTagsBySpaceId(spaceId);
        SpaceTagsGetApiResponse apiResponse = spaceMapper.toSpaceTagsGetApiResponse(response);
        return ResponseEntity
                .ok()
                .body(apiResponse);
    }

    /**
     * 루트 댓글 페이징 조회 API
     */
    @Operation(
            summary = "루트 댓글 페이징 조회 API", description = "루트 댓글 페이징 조회 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회가 성공적으로 완료 되었습니다."),
                    @ApiResponse(responseCode = "404", description = "댓글을 달 수 없는 스페이스의 댓글을 보려고 합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping(
            value = "/{spaceId}/comments",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RootCommentFindApiResponses> findRootComments(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal MemberDetails memberDetails,
            @ModelAttribute RootCommentsFindApiRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize());

        Long myMemberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();

        CommentAndChildCountAndMemberInfoResponses responses = commentFacade.findRootComments(spaceId, myMemberId,
                pageRequest);

        RootCommentFindApiResponses apiResponses = RootCommentFindApiResponses.from(responses);

        return ResponseEntity.ok(apiResponses);
    }

    /**
     * 대댓글 페이징 조회 API
     */
    @Operation(
            summary = "대댓글 페이징 조회 API", description = "대댓글 페이징 조회 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회가 성공적으로 완료 되었습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 스페이스 / 댓글을 막아놓은 스페이스 / 삭제된 루트 댓글 ",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping(
            value = "/{spaceId}/comments/{commentId}/replies",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepliesFindApiResponses> findReplies(
            @PathVariable Long spaceId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal MemberDetails memberDetails,
            @ModelAttribute RepliesFindApiRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize());

        Long myMemberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();

        RepliesAndMemberInfoResponses responses = commentFacade.findReplies(spaceId, commentId, myMemberId,
                pageRequest);

        RepliesFindApiResponses apiResponses = RepliesFindApiResponses.from(responses);

        return ResponseEntity.ok(apiResponses);
    }

    /**
     * 댓글 수정 API
     */
    @Operation(
            summary = "댓글 수정 API", description = "댓글 수정 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "댓글이 성공적으로 수정되었습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 스페이스 / 댓글을 달 수 없는 스페이스 / 존재하지 않는 댓글 / 수정 권한 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "G004: 필수 파라미터 누락 및 서버에서 지원하지 않는 타입 및 제한 보다 큰 사이즈의 파라미터가 요청되었습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorWithDetailCodeResponse.class)))
            })
    @PutMapping("/{spaceId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateApiResponse> updateComment(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateApiRequest request
    ) {
        CommentUpdateRequest apiRequest = commentMapper.toCommentUpdateRequest(spaceId, commentId, memberDetails.memberId(), request.content());
        Long updatedCommentId = commentService.updateComment(apiRequest);

        CommentUpdateApiResponse apiResponse = CommentUpdateApiResponse.from(updatedCommentId);

        return ResponseEntity
                .created(URI.create(SPACE_LOCATION_PRE_FIX + "/commments/" + updatedCommentId))
                .body(apiResponse);
    }

    /**
     * 댓글 삭제 API
     */
    @Operation(
            summary = "댓글 삭제 API", description = "댓글 삭제 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "댓글이 성공적으로 삭제되었습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 스페이스 / 댓글을 달 수 없는 스페이스 / 존재하지 않는 댓글 / 삭제 권한 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{spaceId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(spaceId, commentId, memberDetails.memberId());

        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * 스페이스 즐겨찾기 추가 API
     */
    @Operation(
            summary = "스페이스 즐겨찾기 추가 API", description = "스페이스 즐겨찾기 추가 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스페이스가 성공적으로 즐겨찾기 등록 되었습니다."),
                    @ApiResponse(responseCode = "404",
                            description = "존재하지 않는 스페이스를 즐겨찾기 등록하려고 합니다,\n\n " +
                                    "권한이 없는 스페이스를 즐겨찾기에 등록하려고 합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409",
                    description = "이미 즐겨찾기 된 스페이스 입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorWithDetailCodeResponse.class)))
            })
    @PostMapping(value = "/{spaceId}/favorites",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceRegisterInFavoriteApiResponse> registerSpaceInFavorite(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId
    ) {
        SpaceRegisterInFavoriteResponse response = favoriteService.createFavorite(spaceId, memberDetails.memberId());
        SpaceRegisterInFavoriteApiResponse apiResponse = SpaceRegisterInFavoriteApiResponse.from(response.favoriteCount());

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 테스트용 스페이스 즐겨찾기 (비관락을 이용한 락킹 리드)
     */
    @PostMapping(value = "/{spaceId}/favorites/locking-read",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceRegisterInFavoriteApiResponse> registerSpaceInFavoriteWithLock(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId
    ) {
        SpaceRegisterInFavoriteResponse response = favoriteService.createFavoriteWithLock(spaceId, memberDetails.memberId());
        SpaceRegisterInFavoriteApiResponse apiResponse = SpaceRegisterInFavoriteApiResponse.from(response.favoriteCount());

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 스페이스 즐겨찾기 취소 API
     */
    @Operation(
            summary = "스페이스 즐겨찾기 취소 API", description = "스페이스 즐겨찾기 취소 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "스페이스가 성공적으로 즐겨찾기 등록 되었습니다."),
                    @ApiResponse(responseCode = "404",
                            description = "존재하지 않는 즐겨찾기를 취소하려고 합니다,\n\n " +
                                    "즐겨찾기를 등록한 유저가 아닌 다른 유저가 즐겨찾기를 취소하려고 합니다. (두 예외 응답에 대한 응답값은 Schema를 눌러 확인해주세요!!)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping(value = "/{spaceId}/favorites")
    public ResponseEntity<Void> cancelFavoriteSpace(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId
    ) {
        favoriteService.cancelFavoriteSpace(spaceId, memberDetails.memberId());

        return ResponseEntity.noContent().build();
    }

    /**
     * 나의 즐겨찾기 스페이스 검색 API
     */
    @Operation(
            summary = "나의 즐겨찾기 스페이스 검색 API", description = "내가 즐겨찾기한 스페이스를 keyWord, pageNumber, pageSize, filter를 통해 검색합니다. (keyWord, filter 조건 없이 사용 가능합니다.)\n\n" +
            "해당 API는 keyWord, filter 없이도 사용 가능한 페이징 조회입니다.\n\n" +
            "filter: {ENTER_ART, LIFE_KNOWHOW_SHOPPING, HOBBY_LEISURE_TRAVEL, KNOWLEDGE_ISSUE_CAREER, ETC}",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색이 성공적으로 완료 되었습니다."),
            })
    @GetMapping(value = "/favorites/me",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MyFavoriteSpacesFindApiResponses> findMyFavoriteSpaces(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @ModelAttribute MyFavoriteSpacesFindApiRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize());
        FavoriteSpacesFindResponses responses = favoriteService.findMyFavoriteSpaces(
                favoriteMapper.toMyFavoriteSpacesFindRequest(pageRequest, request, memberDetails.memberId())
        );

        MyFavoriteSpacesFindApiResponses apiResponses = MyFavoriteSpacesFindApiResponses.from(responses);
        return ResponseEntity.ok(apiResponses);
    }

    /**
     * 스페이스를 새로운 스페이스로 가져오기 API
     */
    @Operation(
            summary = "스페이스 가져오기 API(나의 새로운 스페이스로)", description = "스페이스를 나의 새로운 스페이스로 가져오기 API 입니다.\n\n" +
            "스페이스 가져오기는 한 스페이스에 한번만 가능하며 링크 개수 200개 이하인 스페이스만 지원합니다.\n\n " +
            "조건이 충족되지 않으면 아래의 예외가 발생합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "스페이스 가져오기가 성공적으로 완료되었습니다."),
                    @ApiResponse(responseCode = "400, 403", description = "400: (G004: 필수 파라미터 누락 및 서버에서 지원하지 않는 타입 및 제한 보다 큰 사이즈의 파라미터가 요청되었습니다.)\n\n " +
                            "403: (S003: 한 스페이스에 대한 가져오기는 1회만 가능합니다./ S004: 가져오기는 200개 이하의 Link를 가진 스페이스만 가능합니다.)",
                            content = @Content(schema = @Schema(implementation = ErrorWithDetailCodeResponse.class))),
                    @ApiResponse(responseCode = "404",
                    description = "존재하지 않는 스페이스를 가져오기 하려고 합니다,\n\n " +
                            "권한이 없는 스페이스를 가져오기 하려고 합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping(value = "/{spaceId}/scraps/new",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewSpacesScrapApiResponse> scrapAndCreateNewSpace(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId,
            @RequestPart @Valid NewSpacesScrapApiRequest request,
            @RequestPart(required = false) MultipartFile file
    ) {
        NewSpacesScrapFacadeRequest facadeRequest = spaceMapper.toNewSpacesScrapFacadeRequest(request, spaceId, memberDetails.memberId(), file);
        Long responseSpaceId = spaceFacade.scrapAndCreateNewSpace(facadeRequest);

        NewSpacesScrapApiResponse apiResponse = NewSpacesScrapApiResponse.from(responseSpaceId);

        return ResponseEntity
                .created(URI.create(SPACE_LOCATION_PRE_FIX + responseSpaceId))
                .body(apiResponse);
    }

    /**
     *  스페이스 나가기 API
     */
    @Operation(
            summary = "스페이스 나가기 API", description = "스페이스 나가기 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "스페이스 나가기가 성공적으로 수행되었습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 스페이스 / 존재하지 않는 스페이스 멤버",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping(value = "/{spaceId}/join")
    public ResponseEntity<Void> leaveSpace(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long spaceId
    ) {
        spaceService.deleteSpaceMemberByMe(spaceId, memberDetails.memberId());

        return ResponseEntity.noContent().build();
    }

    private void setSpaceViewCookie(HttpServletResponse servletResponse, List<Long> spaceViews) {
        String spaceViewCookieValue = spaceViews.toString()
                .replace(",", "_")
                .replace(" ", "");

        Cookie spaceViewCookie = new Cookie("spaceView", spaceViewCookieValue);
        spaceViewCookie.setPath("/spaces");
        spaceViewCookie.setMaxAge(COOKIE_EXPIRE_TIME);

        servletResponse.addCookie(spaceViewCookie);
    }

}
