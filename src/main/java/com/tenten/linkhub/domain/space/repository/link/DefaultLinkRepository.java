package com.tenten.linkhub.domain.space.repository.link;

import com.tenten.linkhub.domain.space.model.link.Link;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultLinkRepository implements LinkRepository {
    private final LinkJpaRepository linkJpaRepository;

    public DefaultLinkRepository(LinkJpaRepository linkJpaRepository) {
        this.linkJpaRepository = linkJpaRepository;
    }

    @Override
    public Link save(Link link) {
        return linkJpaRepository.save(link);
    }
}
