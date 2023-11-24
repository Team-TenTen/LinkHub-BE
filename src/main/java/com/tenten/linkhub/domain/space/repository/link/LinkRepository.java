package com.tenten.linkhub.domain.space.repository.link;

import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.repository.link.dto.LinkGetDto;
import com.tenten.linkhub.domain.space.repository.link.dto.LinkGetQueryCondition;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    Link save(Link link);

    Link getById(Long linkId);

    Optional<Link> findById(Long linkId);

    Slice<LinkGetDto> getLinksByCondition(LinkGetQueryCondition condition);

    Long countLinkBySpaceId(Long spaceId);

    List<Link> findBySpaceId(Long targetSpaceId);

    Long bulkInsertLinks(List<Link> targetLinks, Long spaceId, Long memberId);
}
