package com.tenten.linkhub.domain.space.repository.invitation;

import com.tenten.linkhub.domain.space.model.space.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceInvitationJpaRepository extends JpaRepository<Invitation, Long> {

}
