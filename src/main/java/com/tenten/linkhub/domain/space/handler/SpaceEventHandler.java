package com.tenten.linkhub.domain.space.handler;

import com.tenten.linkhub.domain.space.handler.dto.SpaceImagesDeleteDto;
import com.tenten.linkhub.domain.space.handler.dto.SpaceIncreaseViewCountDto;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
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
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseSpaceViewCount(SpaceIncreaseViewCountDto increaseViewCountDto){
        spaceRepository.getById(increaseViewCountDto.spaceId())
                .increaseViewCount();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteImageFiles(SpaceImagesDeleteDto imagesDeleteDto) {
        s3Uploader.deleteImages(imagesDeleteDto.spaceImageNames());
    }

}
