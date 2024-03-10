package com.tenten.linkhub.domain.link.repository.linkview;

import com.tenten.linkhub.domain.link.model.LinkViewHistory;

public interface LinkViewRepository {

    boolean existsLinkView(Long linkId, Long memberId);

    LinkViewHistory save(LinkViewHistory linkViewHistory);

    void deleteLinkViewHistory(Long linkId);
}
