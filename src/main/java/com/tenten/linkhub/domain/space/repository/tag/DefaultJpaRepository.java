package com.tenten.linkhub.domain.space.repository.tag;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DefaultJpaRepository implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    public DefaultJpaRepository(TagJpaRepository tagJpaRepository) {
        this.tagJpaRepository = tagJpaRepository;
    }

    @Override
    public List<String> findBySpaceIdAndGroupBySpaceName(Long spaceId) {
        return tagJpaRepository.findBySpaceIdAndGroupBySpaceName(spaceId);
    }
}
