package com.tenten.linkhub.domain.space.controller;

import com.tenten.linkhub.domain.space.controller.dto.SpacesFindByQueryApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.SpacesFindByQueryApiResponses;
import com.tenten.linkhub.domain.space.controller.mapper.SpaceApiMapper;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spaces")
public class SpaceController {

    private final SpaceService spaceService;
    private final SpaceApiMapper mapper;

    public SpaceController(SpaceService spaceService, SpaceApiMapper mapper) {
        this.spaceService = spaceService;
        this.mapper = mapper;
    }

    @RequestMapping("/search")
    public ResponseEntity<SpacesFindByQueryApiResponses> findSpacesByQuery(
            Pageable pageable,
            @ModelAttribute @Valid SpacesFindByQueryApiRequest request
    ){
        SpacesFindByQueryResponses responses = spaceService.findSpacesByQuery(
                mapper.toSpacesFindByQueryRequest(request, pageable)
        );

        SpacesFindByQueryApiResponses apiResponses = SpacesFindByQueryApiResponses.from(responses);
        return ResponseEntity.ok(apiResponses);
    }

}
