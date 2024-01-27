package com.tenten.linkhub.domain.link.repository.tag;

import com.tenten.linkhub.domain.link.model.Tag;
import com.tenten.linkhub.domain.link.repository.tag.dto.TagInfo;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    List<TagInfo> findTagBySpaceId(Long spaceId);

    Optional<Tag> findBySpaceIdAndTagName(Long spaceId, String tagName);

    Tag save(Tag newTag);

    List<Tag> findBySpaceId(Long sourceSpaceId);

    Long bulkInsertTags(List<Tag> sourceTags, Long spaceId);
}
