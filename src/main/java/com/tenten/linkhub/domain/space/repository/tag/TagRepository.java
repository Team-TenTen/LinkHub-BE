package com.tenten.linkhub.domain.space.repository.tag;

import java.util.List;

public interface TagRepository {
    List<String> findBySpaceIdAndGroupBySpaceName(Long spaceId);
}
