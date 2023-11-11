package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;

public interface LinkService {
    Long createLink(LinkCreateRequest request);
}
