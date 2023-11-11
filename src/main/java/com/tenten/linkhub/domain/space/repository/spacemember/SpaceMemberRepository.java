package com.tenten.linkhub.domain.space.repository.spacemember;

public interface SpaceMemberRepository {

    boolean existsAuthorizedSpaceMember(Long memberId, Long spaceId);

}
