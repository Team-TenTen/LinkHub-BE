package com.tenten.linkhub.domain.space.controller;

import com.tenten.linkhub.domain.space.controller.dto.space.SpacesFindByQueryApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.space.SpacesFindByQueryApiResponses;
import com.tenten.linkhub.domain.space.controller.mapper.SpaceApiMapper;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "space", description = "space 템플릿 API Document")
@RestController
@RequestMapping("/spaces")
public class SpaceController {

    private final SpaceService spaceService;
    private final SpaceApiMapper mapper;

    public SpaceController(SpaceService spaceService, SpaceApiMapper mapper) {
        this.spaceService = spaceService;
        this.mapper = mapper;
    }

    @Operation(
            summary = "스페이스 검색 API", description = "keyWord, pageNumber, pageSize, sort, filter를 받아 검색합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색이 성공적으로 완료 되었습니다."),
            })
    @GetMapping(value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpacesFindByQueryApiResponses> findSpacesByQuery(
            Pageable pageable,
            @ModelAttribute SpacesFindByQueryApiRequest request
    ) {
        SpacesFindByQueryResponses responses = spaceService.findSpacesByQuery(
                mapper.toSpacesFindByQueryRequest(request, pageable)
        );

        SpacesFindByQueryApiResponses apiResponses = SpacesFindByQueryApiResponses.from(responses);
        return ResponseEntity.ok(apiResponses);
    }

}
