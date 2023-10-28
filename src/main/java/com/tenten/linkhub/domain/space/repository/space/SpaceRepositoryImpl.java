package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCond;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpaceRepositoryImpl implements SpaceRepository{

    private final SpaceJpaRepository spaceJpaRepository;
    private final SpaceQueryRepository spaceQueryRepository;

    public SpaceRepositoryImpl(SpaceJpaRepository spaceJpaRepository, SpaceQueryRepository spaceQueryRepository) {
        this.spaceJpaRepository = spaceJpaRepository;
        this.spaceQueryRepository = spaceQueryRepository;
    }

    @Override
    public Slice<Space> findByQuery(QueryCond queryCond) {
        return spaceQueryRepository.findByCondition(queryCond);
    }

}
