package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceWithSpaceImage;
import com.tenten.linkhub.domain.space.repository.space.query.SpaceQueryRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultSpaceRepository implements SpaceRepository {

    private final SpaceJpaRepository spaceJpaRepository;
    private final SpaceQueryRepository spaceQueryRepository;

    public DefaultSpaceRepository(SpaceJpaRepository spaceJpaRepository, SpaceQueryRepository spaceQueryRepository) {
        this.spaceJpaRepository = spaceJpaRepository;
        this.spaceQueryRepository = spaceQueryRepository;
    }

    @Override
    public Slice<SpaceWithSpaceImage> findByQuery(QueryCondition queryCondition) {
        return spaceQueryRepository.findByCondition(queryCondition);
    }

    @Override
    public Space save(Space space) {
        return spaceJpaRepository.save(space);
    }

}
