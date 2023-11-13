package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;

public interface LinkService {
    Long createLink(LinkCreateRequest request);

    Boolean createLike(Long linkId, Long memberId);

    void cancelLike(Long linkId, Long memberId);
}
