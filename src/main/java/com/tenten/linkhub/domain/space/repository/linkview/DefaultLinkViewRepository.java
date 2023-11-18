package com.tenten.linkhub.domain.space.repository.linkview;

import com.tenten.linkhub.domain.space.model.link.LinkViewHistory;
import com.tenten.linkhub.domain.space.repository.linkview.query.LinkViewQueryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultLinkViewRepository implements LinkViewRepository {

    private final LinkViewQueryRepository linkViewQueryRepository;
    private final LinkViewJpaRepository linkViewJpaRepository;

    public DefaultLinkViewRepository(LinkViewQueryRepository linkViewQueryRepository,
                                     LinkViewJpaRepository linkViewJpaRepository) {
        this.linkViewQueryRepository = linkViewQueryRepository;
        this.linkViewJpaRepository = linkViewJpaRepository;
    }

    @Override
    public boolean existsLinkView(Long linkId, Long memberId) {
        return linkViewQueryRepository.existsLinkViewHistory(linkId, memberId);
    }

    @Override
    public LinkViewHistory save(LinkViewHistory linkViewHistory) {
        return linkViewJpaRepository.save(linkViewHistory);
    }

}


