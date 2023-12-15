package com.tenten.linkhub.domain.space.repository.spacemember;

import com.tenten.linkhub.domain.space.repository.spacemember.querydsl.SpaceMemberQueryDslRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultSpaceMemberRepository implements SpaceMemberRepository {

    private final SpaceMemberQueryDslRepository spaceMemberQueryDslRepository;

    public DefaultSpaceMemberRepository(SpaceMemberQueryDslRepository spaceMemberQueryDslRepository) {
        this.spaceMemberQueryDslRepository = spaceMemberQueryDslRepository;
    }

    @Override
    public boolean existsAuthorizedSpaceMember(Long memberId, Long spaceId) {
        return spaceMemberQueryDslRepository.existsAuthorizedSpaceMember(memberId, spaceId);
    }

}
