package com.tenten.linkhub.domain.space.repository.spaceimage;

import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultSpaceImageRepository implements SpaceImageRepository{

    private final SpaceImageJpaRepository spaceImageJpaRepository;

    public DefaultSpaceImageRepository(SpaceImageJpaRepository spaceImageJpaRepository) {
        this.spaceImageJpaRepository = spaceImageJpaRepository;
    }

    @Override
    public SpaceImage save(SpaceImage spaceImage) {
        return spaceImageJpaRepository.save(spaceImage);
    }

}
