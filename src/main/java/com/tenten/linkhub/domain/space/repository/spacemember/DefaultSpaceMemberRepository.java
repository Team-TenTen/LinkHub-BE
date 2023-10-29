package com.tenten.linkhub.domain.space.repository.spacemember;

import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultSpaceMemberRepository implements SpaceMemberRepository {

    private final SpaceMemberJpaRepository spaceMemberJpaRepository;

    public DefaultSpaceMemberRepository(SpaceMemberJpaRepository spaceMemberJpaRepository) {
        this.spaceMemberJpaRepository = spaceMemberJpaRepository;
    }

    @Override
    public SpaceMember save(SpaceMember spaceMember) {
        return spaceMemberJpaRepository.save(spaceMember);
    }

}