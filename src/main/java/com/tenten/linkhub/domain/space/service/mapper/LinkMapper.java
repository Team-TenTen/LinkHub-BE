package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.repository.link.dto.LinkGetQueryCondition;
import com.tenten.linkhub.domain.space.service.dto.link.LinksGetByQueryRequest;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {

    public LinkGetQueryCondition toQueryCondition(LinksGetByQueryRequest request) {
        return new LinkGetQueryCondition(
                request.spaceId(),
                request.memberId(),
                request.pageable(),
                request.tagId()
        );
    }
}
