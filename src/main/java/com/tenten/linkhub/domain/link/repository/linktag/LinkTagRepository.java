package com.tenten.linkhub.domain.link.repository.linktag;

import com.tenten.linkhub.domain.link.model.LinkTag;

import java.util.List;
import java.util.Map;

public interface LinkTagRepository {
    List<LinkTag> findByLinkIdIn(List<Long> sourceLinkIds);

    Long bulkInsertLinkTag(List<LinkTag> sourceLinkTags, Map<Long, Long> linkIdMappingMap, Map<Long, Long> tagIdMappingMap);
}
