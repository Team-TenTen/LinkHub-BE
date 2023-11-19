package com.tenten.linkhub.domain.space.repository.link;

import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.global.exception.DataNotFoundException;
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
    public Link getById(Long linkId) {
        return linkJpaRepository
                .findById(linkId)
                .orElseThrow(() -> new DataNotFoundException("linkId에 해당하는 link를 찾을 수 없습니다."));
    }

    @Override
    public Optional<Link> findById(Long linkId) {
        return linkJpaRepository.findById(linkId);
    }

}
