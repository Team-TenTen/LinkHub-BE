package com.tenten.linkhub.domain.link.service;

import com.tenten.linkhub.domain.link.service.dto.LinkCreateRequest;
import com.tenten.linkhub.domain.link.service.dto.LinkGetByQueryResponses;
import com.tenten.linkhub.domain.link.service.dto.LinkUpdateRequest;
import com.tenten.linkhub.domain.link.service.dto.LinksGetByQueryRequest;
import com.tenten.linkhub.domain.link.service.dto.PopularLinksGetByQueryResponses;

public interface LinkService {

    Long createLink(LinkCreateRequest request);

    Long updateLink(LinkUpdateRequest request);

    Boolean createLike(Long linkId, Long memberId);

    void cancelLike(Long linkId, Long memberId);

    void addLinkViewHistory(Long spaceId, Long linkId, Long memberId);

    void deleteLink(Long linkId);

    LinkGetByQueryResponses getLinks(LinksGetByQueryRequest request);

    PopularLinksGetByQueryResponses getPopularLinks(Long memberId);

    void copyLinkBySpaceIdAndPaste(Long targetSpaceId, Long savedSpaceId, Long memberId);
}
