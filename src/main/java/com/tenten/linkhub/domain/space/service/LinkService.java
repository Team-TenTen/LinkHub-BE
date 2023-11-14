package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.link.LinkUpdateRequest;

public interface LinkService {

    Long createLink(LinkCreateRequest request);

    Long updateLink(LinkUpdateRequest request);

    Boolean createLike(Long linkId, Long memberId);

    void cancelLike(Long linkId, Long memberId);
}
