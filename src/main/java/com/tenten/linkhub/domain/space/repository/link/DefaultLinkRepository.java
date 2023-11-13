package com.tenten.linkhub.domain.space.repository.link;

import com.tenten.linkhub.domain.space.model.link.Link;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    @Override
    public Optional<Link> findById(Long linkId) {
        return linkJpaRepository.findById(linkId);
    }
}
