package com.tenten.linkhub.domain.space.repository.linkview;

import com.tenten.linkhub.domain.space.model.link.LinkViewHistory;

public interface LinkViewRepository {

    boolean existsLinkView(Long linkId, Long memberId);

    LinkViewHistory save(LinkViewHistory linkViewHistory);

    void deleteLinkViewHistory(Long linkId);
}
