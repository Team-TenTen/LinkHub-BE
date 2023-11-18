package com.tenten.linkhub.domain.space.repository.linkview;

import com.tenten.linkhub.domain.space.model.link.LinkViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkViewJpaRepository extends JpaRepository<LinkViewHistory, Long> {
}
