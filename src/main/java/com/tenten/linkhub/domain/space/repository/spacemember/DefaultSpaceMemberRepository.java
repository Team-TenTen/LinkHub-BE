package com.tenten.linkhub.domain.space.repository.spacemember;

import com.tenten.linkhub.domain.space.repository.spacemember.query.SpaceMemberQueryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultSpaceMemberRepository implements SpaceMemberRepository {

    private final SpaceMemberQueryRepository spaceMemberQueryRepository;

    public DefaultSpaceMemberRepository(SpaceMemberQueryRepository spaceMemberQueryRepository) {
        this.spaceMemberQueryRepository = spaceMemberQueryRepository;
    }

    @Override
    public boolean existsAuthorizedSpaceMember(Long memberId, Long spaceId) {
        return spaceMemberQueryRepository.existsAuthorizedSpaceMember(memberId, spaceId);
    }

}
