package com.tenten.linkhub.domain.space.repository.tag;

import com.tenten.linkhub.domain.space.model.link.Tag;
import com.tenten.linkhub.domain.space.repository.tag.dto.TagInfo;
import com.tenten.linkhub.domain.space.repository.tag.query.TagQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DefaultJpaRepository implements TagRepository {

    private final TagQueryRepository tagQueryRepository;
    private final TagJpaRepository tagJpaRepository;

    public DefaultJpaRepository(TagQueryRepository tagQueryRepository, TagJpaRepository tagJpaRepository) {
        this.tagQueryRepository = tagQueryRepository;
        this.tagJpaRepository = tagJpaRepository;
    }

    @Override
    public List<TagInfo> findBySpaceIdAndGroupBySpaceName(Long spaceId) {
        return tagQueryRepository.findTagBySpaceIdAndGroupBySpaceName(spaceId);
    }

    @Override
    public boolean existsTagInSpace(Long spaceId, String tagName) {
        return tagQueryRepository.existsTagInSpace(spaceId, tagName);
    }

    @Override
    public Optional<Tag> findBySpaceIdAndSpaceName(Long spaceId, String tagName) {
        return tagJpaRepository.findTagBySpaceIdAndTagName(spaceId, tagName);
    }

    @Override
    public Tag save(Tag newTag) {
        return tagJpaRepository.save(newTag);
    }

}
