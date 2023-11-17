package com.tenten.linkhub.domain.space.repository.tag;

import com.tenten.linkhub.domain.space.repository.tag.dto.TagInfo;
import com.tenten.linkhub.domain.space.repository.tag.query.TagQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DefaultJpaRepository implements TagRepository {

    private final TagQueryRepository tagQueryRepository;

    public DefaultJpaRepository(TagQueryRepository tagQueryRepository) {
        this.tagQueryRepository = tagQueryRepository;
    }

    @Override
    public List<TagInfo> findBySpaceIdAndGroupBySpaceName(Long spaceId) {
        return tagQueryRepository.findTagBySpaceIdAndGroupBySpaceName(spaceId);
    }

}
