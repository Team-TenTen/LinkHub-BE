package com.tenten.linkhub.domain.link.repository.link;

import com.tenten.linkhub.domain.link.model.Link;
import com.tenten.linkhub.domain.link.repository.link.dto.LinkGetDto;
import com.tenten.linkhub.domain.link.repository.link.dto.LinkGetQueryCondition;
import com.tenten.linkhub.domain.link.repository.link.dto.PopularLinkGetDto;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    Link save(Link link);

    Link getById(Long linkId);

    Optional<Link> findById(Long linkId);

    Slice<LinkGetDto> getLinksByCondition(LinkGetQueryCondition condition);

    List<PopularLinkGetDto> getPopularLinks(Long memberId);

    Long countLinkBySpaceId(Long spaceId);

    List<Link> findBySpaceId(Long sourceSpaceId);

    Long bulkInsertLinks(List<Link> sourceLinks, Long spaceId, Long memberId);

    void increaseLikeCount(Long linkId);

    void decreaseLikeCount(Long linkId);
}
