package com.tenten.linkhub.domain.space.handler;

import com.tenten.linkhub.domain.space.handler.dto.LinkDecreaseLikeCountEvent;
import com.tenten.linkhub.domain.space.handler.dto.LinkIncreaseLikeCountEvent;
import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.repository.link.LinkRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LinkEventHandler {

    private final LinkRepository linkRepository;

    public LinkEventHandler(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(
            retryFor = OptimisticLockException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public void increaseLikeCount(LinkIncreaseLikeCountEvent linkIncreaseLikeCountEvent) {
        linkRepository.findById(linkIncreaseLikeCountEvent.linkId())
                .ifPresent(Link::increaseLikeCount);
    }

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(
            retryFor = OptimisticLockException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public void decreaseLikeCount(LinkDecreaseLikeCountEvent linkDecreaseLikeCountEvent) {
        linkRepository.findById(linkDecreaseLikeCountEvent.linkId())
                .ifPresent(Link::decreaseLikeCount);
    }

}
