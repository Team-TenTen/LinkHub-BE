package com.tenten.linkhub.domain.space.repository.linkview;

import com.tenten.linkhub.domain.space.model.link.LinkViewHistory;
import com.tenten.linkhub.domain.space.repository.linkview.querydsl.LinkViewQueryDslRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultLinkViewRepository implements LinkViewRepository {

    private final LinkViewQueryDslRepository linkViewQueryDslRepository;
    private final LinkViewJpaRepository linkViewJpaRepository;

    public DefaultLinkViewRepository(LinkViewQueryDslRepository linkViewQueryDslRepository,
                                     LinkViewJpaRepository linkViewJpaRepository) {
        this.linkViewQueryDslRepository = linkViewQueryDslRepository;
        this.linkViewJpaRepository = linkViewJpaRepository;
    }

    @Override
    public boolean existsLinkView(Long linkId, Long memberId) {
        return linkViewQueryDslRepository.existsLinkViewHistory(linkId, memberId);
    }

    @Override
    public LinkViewHistory save(LinkViewHistory linkViewHistory) {
        return linkViewJpaRepository.save(linkViewHistory);
    }

    @Override
    public void deleteLinkViewHistory(Long linkId) {
        linkViewJpaRepository.deleteByLinkId(linkId);
    }

}


