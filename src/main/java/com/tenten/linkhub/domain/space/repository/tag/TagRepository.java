package com.tenten.linkhub.domain.space.repository.tag;

import com.tenten.linkhub.domain.space.repository.tag.dto.TagInfo;

import java.util.List;

public interface TagRepository {
    List<TagInfo> findBySpaceIdAndGroupBySpaceName(Long spaceId);
}
