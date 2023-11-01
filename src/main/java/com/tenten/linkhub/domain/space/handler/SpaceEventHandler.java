package com.tenten.linkhub.domain.space.handler;

import com.tenten.linkhub.domain.space.handler.dto.SpaceIncreaseViewCountDto;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SpaceEventHandler {

    private final SpaceRepository spaceRepository;

    public SpaceEventHandler(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseSpaceViewCount(SpaceIncreaseViewCountDto increaseViewCountDto){
        spaceRepository.getById(increaseViewCountDto.spaceId())
                .increaseViewCount();
    }

}
