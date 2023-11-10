package com.tenten.linkhub.domain.space.repository.tag;

import org.springframework.stereotype.Repository;

@Repository
public class DefaultJpaRepository implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    public DefaultJpaRepository(TagJpaRepository tagJpaRepository) {
        this.tagJpaRepository = tagJpaRepository;
    }
}
