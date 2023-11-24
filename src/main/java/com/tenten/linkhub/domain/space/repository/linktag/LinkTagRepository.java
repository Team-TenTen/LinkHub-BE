package com.tenten.linkhub.domain.space.repository.linktag;

import com.tenten.linkhub.domain.space.model.link.LinkTag;

import java.util.List;
import java.util.Map;

public interface LinkTagRepository {
    List<LinkTag> findByLinkIdIn(List<Long> sourceLinkIds);

    Long bulkInsertLinkTag(List<LinkTag> sourceLinkTags, Map<Long, Long> linkIdMappingMap, Map<Long, Long> tagIdMappingMap);
}
