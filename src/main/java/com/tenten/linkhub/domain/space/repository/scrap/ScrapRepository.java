package com.tenten.linkhub.domain.space.repository.scrap;

import com.tenten.linkhub.domain.space.model.space.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Boolean existsBySpaceIdAndMemberId(Long spaceId, Long memberId);
}
