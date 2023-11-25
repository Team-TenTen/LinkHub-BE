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
    private final TagJdbcRepository tagJdbcRepository;

    public DefaultJpaRepository(TagQueryRepository tagQueryRepository, TagJpaRepository tagJpaRepository, TagJdbcRepository tagJdbcRepository) {
        this.tagQueryRepository = tagQueryRepository;
        this.tagJpaRepository = tagJpaRepository;
        this.tagJdbcRepository = tagJdbcRepository;
    }

    @Override
    public List<TagInfo> findTagBySpaceId(Long spaceId) {
        return tagQueryRepository.findTagBySpaceId(spaceId);
    }

    @Override
    public Optional<Tag> findBySpaceIdAndTagName(Long spaceId, String tagName) {
        return tagJpaRepository.findTagBySpaceIdAndTagName(spaceId, tagName);
    }

    @Override
    public Tag save(Tag newTag) {
        return tagJpaRepository.save(newTag);
    }

    @Override
    public List<Tag> findBySpaceId(Long sourceSpaceId) {
        return tagJpaRepository.findBySpaceId(sourceSpaceId);
    }

    @Override
    public Long bulkInsertTags(List<Tag> sourceTags, Long spaceId) {
        return tagJdbcRepository.bulkInsertTags(sourceTags, spaceId);
    }

}
