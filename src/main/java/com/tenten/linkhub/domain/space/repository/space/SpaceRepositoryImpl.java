package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public class SpaceRepositoryImpl implements SpaceRepository{

    private final SpaceJpaRepository spaceJpaRepository;
    private final SpaceQueryRepository spaceQueryRepository;

    public SpaceRepositoryImpl(SpaceJpaRepository spaceJpaRepository, SpaceQueryRepository spaceQueryRepository) {
        this.spaceJpaRepository = spaceJpaRepository;
        this.spaceQueryRepository = spaceQueryRepository;
    }

    @Override
    public Slice<Space> findByQuery(QueryCondition queryCondition) {
        return spaceQueryRepository.findByCondition(queryCondition);
    }

}
