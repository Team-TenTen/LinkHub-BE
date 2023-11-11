package com.tenten.linkhub.domain.space.repository.link;

import com.tenten.linkhub.domain.space.model.link.Link;
import jakarta.persistence.EntityNotFoundException;
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

    @Override
    public Link getById(Long linkId) {
        return linkJpaRepository
                .findById(linkId)
                .orElseThrow(() -> new EntityNotFoundException("linkId에 해당하는 link를 찾을 수 없습니다."));
    }

}
