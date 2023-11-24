package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.link.LinkGetByQueryResponses;
import com.tenten.linkhub.domain.space.service.dto.link.LinkUpdateRequest;
import com.tenten.linkhub.domain.space.service.dto.link.LinksGetByQueryRequest;

public interface LinkService {

    Long createLink(LinkCreateRequest request);

    Long updateLink(LinkUpdateRequest request);

    Boolean createLike(Long linkId, Long memberId);

    void cancelLike(Long linkId, Long memberId);

    void addLinkViewHistory(Long spaceId, Long linkId, Long memberId);

    void deleteLink(Long linkId);

    LinkGetByQueryResponses getLinks(LinksGetByQueryRequest request);

    void copyLinkBySpaceIdAndPaste(Long targetSpaceId, Long savedSpaceId, Long memberId);
}
