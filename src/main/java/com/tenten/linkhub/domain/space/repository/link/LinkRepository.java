package com.tenten.linkhub.domain.space.repository.link;

import com.tenten.linkhub.domain.space.model.link.Link;

import java.util.Optional;

public interface LinkRepository {
    Link save(Link link);

    Optional<Link> findById(Long linkId);
}
