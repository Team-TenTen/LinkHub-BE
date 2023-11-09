package com.tenten.linkhub.domain.space.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.space.controller.dto.MySpacesFindApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.MySpacesFindApiResponses;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceUpdateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceUpdateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.SpaceDetailGetByIdApiResponse;
import com.tenten.linkhub.domain.space.controller.dto.space.SpacesFindByQueryApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpacesFindByQueryApiResponses;
import com.tenten.linkhub.domain.space.controller.mapper.SpaceApiMapper;
import com.tenten.linkhub.domain.space.facade.SpaceFacade;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.SpaceDetailGetByIdFacadeResponse;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import com.tenten.linkhub.global.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;

@Tag(name = "spaces", description = "space 템플릿 API Document")
@RestController
@RequestMapping("/spaces")
public class SpaceController {
    private static final String SPACE_LOCATION_PRE_FIX = "https://api.Link-hub.site/spaces/";

    private final SpaceFacade spaceFacade;
    private final SpaceService spaceService;
    private final SpaceApiMapper mapper;

    public SpaceController(SpaceFacade spaceFacade, SpaceService spaceService, SpaceApiMapper mapper) {
        this.spaceFacade = spaceFacade;
        this.spaceService = spaceService;
        this.mapper = mapper;
    }

    /**
     * 스페이스 검색 API
     */
    @Operation(
            summary = "스페이스 검색 API", description = "keyWord, pageNumber, pageSize, sort, filter를 받아 검색합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색이 성공적으로 완료 되었습니다."),
            })
    @GetMapping(value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpacesFindByQueryApiResponses> findSpacesByQuery(
            @ModelAttribute SpacesFindByQueryApiRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(
                request.pageNumber(),
                request.pageSize(),
                request.sort() != null ? Sort.by(request.sort()) : Sort.unsorted()
        );

        SpacesFindByQueryResponses responses = spaceService.findSpacesByQuery(
                mapper.toSpacesFindByQueryRequest(request, pageRequest)
        );

        SpacesFindByQueryApiResponses apiResponses = SpacesFindByQueryApiResponses.from(responses);
        return ResponseEntity.ok(apiResponses);
    }

    /**
     * 스페이스 생성 API
     */
    @Operation(
            summary = "스페이스 생성 API", description = "스페이스 생성 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "스페이스가 성공적으로 생성되었습니다.")
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SpaceCreateApiResponse> createSpace(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @Parameter(
                    description = "이미지 파일 외의 데이터는 application/json 타입으로 받습니다.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestPart @Valid SpaceCreateApiRequest request,
            @RequestPart(required = false) MultipartFile file
    ) {
        Long savedSpaceId = spaceFacade.createSpace(mapper.toSpaceCreateFacadeRequest(request, file, memberDetails.memberId()));

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
                    @ApiResponse(responseCode = "404", description = "요청한 spaceId에 해당하는 스페이스를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping(value = "/{spaceId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceDetailGetByIdApiResponse> getSpaceDetailById(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @Parameter(hidden = true)
            @CookieValue(value = "spaceView", required = false) Cookie spaceViewCookie,
            @PathVariable Long spaceId,
            HttpServletResponse servletResponse
    ) {
        Long memberId = Objects.isNull(memberDetails) ? null : memberDetails.memberId();
        SpaceDetailGetByIdFacadeRequest request = mapper.toSpaceDetailGetByIdFacadeRequest(spaceId, spaceViewCookie, memberId);

        SpaceDetailGetByIdFacadeResponse response = spaceFacade.getSpaceDetailById(request);

        servletResponse.addCookie(response.spaceViewCookie());
        SpaceDetailGetByIdApiResponse apiResponse = SpaceDetailGetByIdApiResponse.from(response);

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 스페이스 수정 API
     */
    @Operation(
            summary = "스페이스 정보 수정 API", description = "스페이스 정보 수정 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스페이스가 성공적으로 수정되었습니다."),
                    @ApiResponse(responseCode = "404", description = "권한이 없는 유저가 스페이스를 수정하려고 합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PatchMapping(value = "/{spaceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
                mapper.toSpaceUpdateFacadeRequest(spaceId, request, file, memberDetails.memberId()));

        SpaceUpdateApiResponse apiResponse = SpaceUpdateApiResponse.from(updatedSpaceId);

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
     *  내 스페이스 검색 API
     *  !필터에 해당 API 추가해야 함!
     */
    @Operation(
            summary = "내 스페이스 검색 API", description = "나의 스페이스를 keyWord, pageNumber, pageSize, filter를 통해 검색합니다.\n" +
            "해당 API는 keyWord, filter 없이 사용 가능한 페이징 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색이 성공적으로 완료 되었습니다."),
            })
    @GetMapping("/search/me")
    public ResponseEntity<MySpacesFindApiResponses> findMySpaces(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @ModelAttribute MySpacesFindApiRequest request
    ){
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize());

        SpacesFindByQueryResponses responses = spaceService.findMySpacesByQuery(
                mapper.toMySpacesFindRequest(pageRequest, request, memberDetails.memberId())
        );

        MySpacesFindApiResponses apiResponses = MySpacesFindApiResponses.from(responses);
        return ResponseEntity.ok(apiResponses);
    }

}
