package com.tenten.linkhub.domain.space.repository.tag;

import com.tenten.linkhub.domain.space.model.link.Tag;
import com.tenten.linkhub.domain.space.repository.tag.dto.TagInfo;
import com.tenten.linkhub.domain.space.repository.tag.querydsl.TagQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DefaultTagRepository implements TagRepository {

    private final TagQueryDslRepository tagQueryDslRepository;
    private final TagJpaRepository tagJpaRepository;
    private final TagJdbcRepository tagJdbcRepository;

    public DefaultTagRepository(TagQueryDslRepository tagQueryDslRepository, TagJpaRepository tagJpaRepository, TagJdbcRepository tagJdbcRepository) {
        this.tagQueryDslRepository = tagQueryDslRepository;
        this.tagJpaRepository = tagJpaRepository;
        this.tagJdbcRepository = tagJdbcRepository;
    }

    @Override
    public List<TagInfo> findTagBySpaceId(Long spaceId) {
        return tagQueryDslRepository.findTagBySpaceId(spaceId);
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
