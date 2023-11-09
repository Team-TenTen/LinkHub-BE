package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.MySpacesFindQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.repository.space.query.SpaceQueryRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public Slice<Space> findSpacesJoinSpaceImageByQuery(QueryCondition queryCondition) {
        return spaceQueryRepository.findSpacesJoinSpaceImageByCondition(queryCondition);
    }

    @Override
    public Space save(Space space) {
        return spaceJpaRepository.save(space);
    }

    @Override
    public Space getById(Long spaceId) {
        return spaceJpaRepository.findById(spaceId)
                .orElseThrow(() -> new EntityNotFoundException("해당 spaceId를 가진 Space를 찾을 수 없습니다."));
    }

    @Override
    public Space getSpaceJoinSpaceMemberById(Long spaceId) {
        return spaceJpaRepository.findSpaceJoinSpaceMemberById(spaceId)
                .orElseThrow(() -> new EntityNotFoundException("해당 spaceId를 가진 SpaceWithSpaceImage를 찾을 수 없습니다."));
    }

    @Override
    public Slice<Space> findMySpacesJoinSpaceImageByQuery(MySpacesFindQueryCondition queryCondition) {
        return spaceQueryRepository.findMySpacesJoinSpaceImageByCondition(queryCondition);
    }

}
