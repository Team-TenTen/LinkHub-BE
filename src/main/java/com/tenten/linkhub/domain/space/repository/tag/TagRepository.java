package com.tenten.linkhub.domain.space.repository.tag;

import com.tenten.linkhub.domain.space.model.link.Tag;
import com.tenten.linkhub.domain.space.repository.tag.dto.TagInfo;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    List<TagInfo> findBySpaceId(Long spaceId);

    Optional<Tag> findBySpaceIdAndTagName(Long spaceId, String tagName);

    Tag save(Tag newTag);
}
