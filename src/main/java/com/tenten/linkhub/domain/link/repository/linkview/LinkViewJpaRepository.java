package com.tenten.linkhub.domain.link.repository.linkview;

import com.tenten.linkhub.domain.link.model.LinkViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkViewJpaRepository extends JpaRepository<LinkViewHistory, Long> {
    void deleteByLinkId(Long linkId);
}
