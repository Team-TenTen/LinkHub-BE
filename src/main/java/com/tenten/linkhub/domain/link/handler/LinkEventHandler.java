package com.tenten.linkhub.domain.link.handler;

import com.tenten.linkhub.domain.link.handler.dto.LinkDecreaseLikeCountEvent;
import com.tenten.linkhub.domain.link.handler.dto.LinkIncreaseLikeCountEvent;
import com.tenten.linkhub.domain.link.repository.link.LinkRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
public class LinkEventHandler {

    private final LinkRepository linkRepository;

    public LinkEventHandler(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void increaseLikeCount(LinkIncreaseLikeCountEvent event) {
        linkRepository.increaseLikeCount(event.linkId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteLikeCount(LinkDecreaseLikeCountEvent event) {
        linkRepository.decreaseLikeCount(event.linkId());
    }

}
