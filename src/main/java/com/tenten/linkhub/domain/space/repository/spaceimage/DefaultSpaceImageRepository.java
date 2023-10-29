package com.tenten.linkhub.domain.space.repository.spaceimage;

import org.springframework.stereotype.Repository;

@Repository
public class DefaultSpaceImageRepository implements SpaceImageRepository{

    private final SpaceImageJpaRepository spaceImageJpaRepository;

    public DefaultSpaceImageRepository(SpaceImageJpaRepository spaceImageJpaRepository) {
        this.spaceImageJpaRepository = spaceImageJpaRepository;
    }

}
