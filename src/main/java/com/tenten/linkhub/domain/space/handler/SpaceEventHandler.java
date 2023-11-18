package com.tenten.linkhub.domain.space.handler;

import com.tenten.linkhub.domain.space.handler.dto.SpaceImagesDeleteEvent;
import com.tenten.linkhub.domain.space.handler.dto.SpaceIncreaseFavoriteCountEvent;
import com.tenten.linkhub.domain.space.handler.dto.SpaceIncreaseViewCountEvent;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SpaceEventHandler {

    private final SpaceRepository spaceRepository;
    private final S3Uploader s3Uploader;

    public SpaceEventHandler(SpaceRepository spaceRepository, S3Uploader s3Uploader) {
        this.spaceRepository = spaceRepository;
        this.s3Uploader = s3Uploader;
    }

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseSpaceViewCount(SpaceIncreaseViewCountEvent event){
        spaceRepository.getById(event.spaceId())
                .increaseViewCount();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteImageFiles(SpaceImagesDeleteEvent event) {
        s3Uploader.deleteImages(event.spaceImageNames());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseFavoriteCount (SpaceIncreaseFavoriteCountEvent event) {
        spaceRepository.increaseFavoriteCount(event.spaceId());
    }

}
