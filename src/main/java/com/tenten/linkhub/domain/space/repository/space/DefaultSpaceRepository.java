package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.common.SpaceCursorSlice;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.common.dto.SpaceAndSpaceImageOwnerNickName;
import com.tenten.linkhub.domain.space.repository.space.dto.CursorPageQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.MemberSpacesQueryCondition;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCondition;
import com.tenten.linkhub.domain.space.repository.space.querydsl.SpaceQueryDslRepository;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DefaultSpaceRepository implements SpaceRepository {

    private final SpaceJpaRepository spaceJpaRepository;
    private final SpaceQueryDslRepository spaceQueryDslRepository;

    public DefaultSpaceRepository(SpaceJpaRepository spaceJpaRepository, SpaceQueryDslRepository spaceQueryDslRepository) {
        this.spaceJpaRepository = spaceJpaRepository;
        this.spaceQueryDslRepository = spaceQueryDslRepository;
    }

    @Override
    public SpaceCursorSlice<SpaceAndSpaceImageOwnerNickName> findPublicSpacesJoinSpaceImageByQuery(CursorPageQueryCondition queryCondition) {
        return spaceQueryDslRepository.findPublicSpacesJoinSpaceImageByCondition(queryCondition);
    }

    @Override
    public Slice<SpaceAndSpaceImageOwnerNickName> searchPublicSpacesJoinSpaceImageByQuery(QueryCondition queryCondition) {
        return spaceQueryDslRepository.searchPublicSpacesJoinSpaceImageByCondition(queryCondition);
    }

    @Override
    public Space save(Space space) {
        return spaceJpaRepository.save(space);
    }

    @Override
    public Space getById(Long spaceId) {
        return spaceJpaRepository.findById(spaceId)
                .orElseThrow(() -> new DataNotFoundException("해당 spaceId를 가진 Space를 찾을 수 없습니다."));
    }

    @Override
    public Space getSpaceJoinSpaceMemberById(Long spaceId) {
        return spaceJpaRepository.findSpaceJoinSpaceMemberById(spaceId)
                .orElseThrow(() -> new DataNotFoundException("해당 spaceId를 가진 SpaceWithSpaceImage를 찾을 수 없습니다."));
    }

    @Override
    public Slice<SpaceAndSpaceImageOwnerNickName> findMemberSpacesJoinSpaceImageByQuery(MemberSpacesQueryCondition queryCondition) {
        return spaceQueryDslRepository.findMemberSpacesJoinSpaceImageByCondition(queryCondition);
    }

    @Override
    @Transactional
    public void increaseFavoriteCount(Long spaceId) {
        spaceJpaRepository.increaseFavoriteCount(spaceId);
    }

    @Override
    @Transactional
    public void decreaseFavoriteCount(Long spaceId) {
        spaceJpaRepository.decreaseFavoriteCount(spaceId);
    }

    @Override
    public void increaseScrapCount(Long spaceId) {
        spaceJpaRepository.increaseScrapCount(spaceId);
    }

}
