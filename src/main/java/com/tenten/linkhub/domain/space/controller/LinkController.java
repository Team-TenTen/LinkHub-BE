package com.tenten.linkhub.domain.space.controller;

import com.tenten.linkhub.domain.auth.MemberDetails;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiRequest;
import com.tenten.linkhub.domain.space.controller.dto.link.LinkCreateApiResponse;
import com.tenten.linkhub.domain.space.controller.mapper.LinkApiMapper;
import com.tenten.linkhub.domain.space.facade.LinkFacade;
import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.service.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LinkController {
    private static final String LINK_LOCATION_PREFIX = "https://api.Link-hub.site/links/";

    private final LinkService linkService;
    private final LinkFacade linkFacade;
    private final LinkApiMapper mapper;

    public LinkController(LinkService linkService, LinkFacade linkFacade, LinkApiMapper mapper) {
        this.linkService = linkService;
        this.linkFacade = linkFacade;
        this.mapper = mapper;
    }

    /**
     * 링크 생성 API
     */
    @PostMapping("/spaces/{spaceId}/links")
    public ResponseEntity<LinkCreateApiResponse> createLink(
            @PathVariable Long spaceId,
            @RequestBody LinkCreateApiRequest apiRequest,
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
}
